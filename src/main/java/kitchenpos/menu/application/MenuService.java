package kitchenpos.menu.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuDao;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupDao;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductDao;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductDao;

@Service
public class MenuService {
	private final MenuDao menuDao;
	private final MenuGroupDao menuGroupDao;
	private final MenuProductDao menuProductDao;
	private final ProductDao productDao;

	public MenuService(
		final MenuDao menuDao,
		final MenuGroupDao menuGroupDao,
		final MenuProductDao menuProductDao,
		final ProductDao productDao
	) {
		this.menuDao = menuDao;
		this.menuGroupDao = menuGroupDao;
		this.menuProductDao = menuProductDao;
		this.productDao = productDao;
	}

	@Transactional
	public MenuResponse create(final MenuRequest request) {
		final BigDecimal price = request.getPrice();

		if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException();
		}

		MenuGroup menuGroup = menuGroupDao.findById(request.getMenuGroupId())
			.orElseThrow(IllegalArgumentException::new);

		final List<MenuProductRequest> menuProducts = request.getMenuProducts();

		BigDecimal sum = BigDecimal.ZERO;
		for (final MenuProductRequest menuProductRequest : menuProducts) {
			final Product product = productDao.findById(menuProductRequest.getProductId())
				.orElseThrow(IllegalArgumentException::new);
			sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProductRequest.getQuantity())));
		}

		if (price.compareTo(sum) > 0) {
			throw new IllegalArgumentException();
		}

		final Menu savedMenu = menuDao.save(request.toMenu(menuGroup));

		final List<MenuProduct> savedMenuProducts = new ArrayList<>();
		for (final MenuProductRequest menuProductRequest : menuProducts) {
			final Product product = productDao.findById(menuProductRequest.getProductId())
				.orElseThrow(IllegalArgumentException::new);
			savedMenuProducts.add(menuProductDao.save(new MenuProduct.Builder()
				.menu(savedMenu)
				.product(product)
				.quantity(menuProductRequest.getQuantity())
				.build()));
		}
		savedMenu.setMenuProducts(savedMenuProducts);

		return MenuResponse.from(savedMenu);
	}

	public List<MenuResponse> list() {
		final List<Menu> menus = menuDao.findAll();

		for (final Menu menu : menus) {
			menu.setMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
		}

		return MenuResponse.newList(menus);
	}
}

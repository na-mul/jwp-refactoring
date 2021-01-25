package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;

public class MenuRequest {
	private String name;
	private BigDecimal price;
	private Long menuGroupId;
	private List<MenuProductRequest> menuProducts;

	public MenuRequest() {
	}

	public MenuRequest(String name, BigDecimal price, Long menuGroupId,
		List<MenuProductRequest> menuProducts) {
		this.name = name;
		this.price = price;
		this.menuGroupId = menuGroupId;
		this.menuProducts = menuProducts;
	}

	public String getName() {
		return name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public Long getMenuGroupId() {
		return menuGroupId;
	}

	public List<MenuProductRequest> getMenuProducts() {
		return menuProducts;
	}

	public Menu toMenu(MenuGroup menuGroup) {
		return new Menu.Builder()
			.name(name)
			.price(price)
			.menuGroup(menuGroup)
			.build();
	}
}

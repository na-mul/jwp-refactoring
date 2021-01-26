package kitchenpos.table.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.util.CollectionUtils;

@Entity
public class TableGroup {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private LocalDateTime createdDate;
	@OneToMany(mappedBy = "tableGroup")
	private List<OrderTable> orderTables = new ArrayList<>();

	public TableGroup() {
	}

	public TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
		validate(orderTables);

		this.id = id;
		this.createdDate = createdDate;
		this.orderTables = orderTables;

		belongToGroup(orderTables);
	}

	private void validate(List<OrderTable> orderTables) {
		if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
			throw new IllegalArgumentException("주문 테이블이 2개 이상이어야 단체로 지정될 수 있습니다.");
		}
		boolean isInGroupOrNotEmpty = orderTables.stream().anyMatch(it -> !it.isEmpty() || it.getTableGroup() != null);
		if (isInGroupOrNotEmpty) {
			throw new IllegalArgumentException("주문 테이블이 비어있지 않거나 이미 단체 지정이 되어있습니다.");
		}
		if (new HashSet<>(orderTables).size() != orderTables.size()) {
			throw new IllegalArgumentException("주문 테이블이 중복됩니다.");
		}
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public List<OrderTable> getOrderTables() {
		return orderTables;
	}

	private void belongToGroup(List<OrderTable> orderTables) {
		if (orderTables == null) {
			return;
		}
		for (OrderTable orderTable : orderTables) {
			orderTable.belongToGroup(this);
		}
	}

	public static final class Builder {
		private Long id;
		private LocalDateTime createdDate;
		private List<OrderTable> orderTables = new ArrayList<>();

		public Builder() {
		}

		public Builder id(Long id) {
			this.id = id;
			return this;
		}

		public Builder createdDate(LocalDateTime createdDate) {
			this.createdDate = createdDate;
			return this;
		}

		public Builder orderTables(List<OrderTable> orderTables) {
			this.orderTables = orderTables;
			return this;
		}

		public Builder orderTables(OrderTable... orderTables) {
			this.orderTables = Arrays.asList(orderTables);
			return this;
		}

		public TableGroup build() {
			return new TableGroup(id, createdDate, orderTables);
		}
	}
}

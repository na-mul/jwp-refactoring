package kitchenpos.order.dto;

import java.util.List;

import kitchenpos.order.domain.OrderStatus;

public class OrderRequest {
	private Long orderTableId;
	private OrderStatus orderStatus;
	private List<OrderLineItemRequest> orderLineItems;

	public OrderRequest() {
	}

	public OrderRequest(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
		this.orderTableId = orderTableId;
		this.orderLineItems = orderLineItems;
	}

	public Long getOrderTableId() {
		return orderTableId;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public List<OrderLineItemRequest> getOrderLineItems() {
		return orderLineItems;
	}
}

package kitchenpos.dao;

import kitchenpos.domain.Menu;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuDao extends JpaRepository<Menu, Long> {
    long countByIdIn(List<Long> ids);
}

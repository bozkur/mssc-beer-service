package guru.springframework.msscbeerservice.services.inventory;

import java.util.UUID;

/**
 * @author cevher
 */
public interface BeerInventoryService {

    Integer getOnHandInventory(UUID beerId);
}

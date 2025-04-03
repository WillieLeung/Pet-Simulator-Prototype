package testing;

import logic.Actions;
import logic.GameInventory;
import logic.Pet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestIntegration {

    @Test
    public void testFeedingPetDepletesInventoryAndUpdatesFullness() {
        // Initialize pet and inventory
        GameInventory inventory = new GameInventory("Drake", true);
        Pet pet = new Pet(100, 100, 100, 100, 100, "BOB", "Moody", "Sprite", inventory);
        
        // Initialize Actions
        Actions actions = new Actions(1, 2, 3, 4, 5, 6);

        // Feed the pet
        actions.feedPet(pet, "Pizza", 10);
        
        // Check if pet's fullness increased
        assertEquals(110, pet.getFullness());  // Fullness should increase by 10
        
        // Check if pizza is depleted from the inventory
        assertEquals(4, inventory.getFoodItems().get("Pizza"));  // Pizza should be reduced by 10
    }

    @Test
    public void testDepletingGiftItemUpdatesHappinessAndInventory() {
        // Initialize pet and inventory
        GameInventory inventory = new GameInventory("Drake", true);
        Pet pet = new Pet(100, 100, 100, 100, 100, "BOB", "Moody", "Sprite", inventory);
        
        // Initialize Actions
        Actions actions = new Actions(1, 2, 3, 4, 5, 6);

        // Give the pet a gift
        actions.giftPet(pet, "Ball", 10);  // Give pet a "Ball"
        
        // Check if pet's happiness increased
        assertEquals(110, pet.getHappiness());  // Happiness should increase by 10
        
        // Check if the gift item is depleted from the inventory
        assertEquals(1, inventory.getGiftItems().get("Ball"));  // "Ball" should be depleted
    }
}
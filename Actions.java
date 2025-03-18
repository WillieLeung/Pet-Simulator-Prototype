public class Actions {
    private int[] actionCooldowns;

    public Actions(int[] cooldowns) {
        this.actionCooldowns = cooldowns;
    }

    public void feedPet(Pet pet, String food){
        if(actionCooldowns[0] == 0) {
            pet.setFullness(pet.getFullness() + 10);
            pet.setScore(pet.getScore() + 10);
        }
    }

    public void fivePetGift(Pet pet, String gift){
        if(actionCooldowns[1] == 0) {
            pet.setHappiness(pet.getHappiness() + 5);
            pet.setScore(pet.getScore() + 10);
        }
    }

    public void takePetToVet(Pet pet){
        if(actionCooldowns[2] == 0) {
            pet.setHealth(pet.getHealth() + 10);
            pet.setScore(pet.getScore() + 10);
        }
    }

    public void sleepPet(Pet pet){
        if(actionCooldowns[3] == 0) {
            pet.setSleep(pet.getSleepiness() + 20);
            pet.setScore(pet.getScore() + 10);
        }
    }

    public void playWithPet(Pet pet){
        if(actionCooldowns[4] == 0) {
            pet.setHappiness(pet.getHappiness() + 10);
            pet.setScore(pet.getScore() + 10);
        }
    }

    public void excercisePet(Pet pet){
        if(actionCooldowns[5] == 0) {
            pet.setHealth(pet.getHealth() + 10);
            pet.setScore(pet.getScore() + 10);
        }
    }
}

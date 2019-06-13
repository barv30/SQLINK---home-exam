package findKitchen;

public class SpaceLocationDetails {

    int sumOfDistance;
    int accessible;
    int number;


    SpaceLocationDetails(int number)
    {
        this.number=number;
    }

    public int getNumber() {
        return number;
    }



    public int getAccessible() {
        return accessible;
    }

    public int getSumOfDistance() {
        return sumOfDistance;
    }

    public void setAccessible(int accessible) {
        this.accessible = accessible;
    }

    public void setSumOfDistance(int sumOfDistance) {
        this.sumOfDistance = sumOfDistance;
    }
}

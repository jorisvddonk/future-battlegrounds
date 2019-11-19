package futurebattlegrounds;

public class ShipState {
  private double hull;
  private double battery;
  private double maxBattery;
  private double batteryCooldown;

  public ShipState(double hull, double battery) {
    this.hull = hull;
    this.battery = battery;
    this.maxBattery = battery;
    this.batteryCooldown = 0;
  }

  /**
   * @return the battery
   */
  public double getBattery() {
    return Math.floor(battery);
  }

  /**
   * @return the maxBattery
   */
  public double getMaxBattery() {
    return maxBattery;
  }

  /**
   * @return the hull
   */
  public double getHull() {
    return hull;
  }

  protected void reduceHull(double hulldmg) {
    this.hull -= hulldmg;
  }

  protected boolean canReduceBattery(double battery) {
    if (this.getBattery() - battery > 0 && this.batteryCooldown <= 0) {
      this.battery -= battery;
      this.batteryCooldown = Constants.SHIP_BATTERY_COOLDOWN;
      return true;
    }
    return false;
  }

  protected void tick(double seconds) {
    this.battery += seconds * Constants.SHIP_BATTERY_RECHARGE_PER_SECOND;
    if (this.batteryCooldown > 0) {
      this.batteryCooldown -= seconds;
    }
  }

}
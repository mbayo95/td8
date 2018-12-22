package beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import business.BusinessSpring;
import business.engine.Simulation;
import business.model.SimulationEntry;

/**
 * Simulation bean controller used to collect simulation entry parameters and to
 * start the simulation.
 * 
 * We use proxy pattern to avoid redundant code copy.
 */
@ManagedBean
@SessionScoped
public class EntryBean{
   
    /**
     * Proxy encapsulated object.
     */
    private SimulationEntry entry = new SimulationEntry();;

    private int id;

    public EntryBean() {
    }

    public String startSimulation() {
        Simulation simulation = (Simulation) BusinessSpring
                .getBean("simulation");
        simulation.setEntry(entry);
        id = simulation.simulate();
        return "result";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSimulationDuration() {
        return entry.getSimulationDuration();
    }

    public void setSimulationDuration(int simulationDuration) {
        entry.setSimulationDuration(simulationDuration);
    }

    public int getCashierCount() {
        return entry.getCashierCount();
    }

    public void setCashierCount(int cashierCount) {
        entry.setCashierCount(cashierCount);
    }

    public int getMinServiceTime() {
        return entry.getMinServiceTime();
    }

    public void setMinServiceTime(int minServiceTime) {
        entry.setMinServiceTime(minServiceTime);
    }

    public int getMaxServiceTime() {
        return entry.getMaxServiceTime();
    }

    public void setMaxServiceTime(int maxServiceTime) {
        entry.setMaxServiceTime(maxServiceTime);
    }

    public int getClientArrivalInterval() {
        return entry.getClientArrivalInterval();
    }

    public void setClientArrivalInterval(int clientArrivalInterval) {
        entry.setClientArrivalInterval(clientArrivalInterval);
    }

    public double getPriorityClientRate() {
        return entry.getPriorityClientRate();
    }

    public void setPriorityClientRate(double priorityClientRate) {
        entry.setPriorityClientRate(priorityClientRate);
    }

    public int getClientPatienceTime() {
        return entry.getClientPatienceTime();
    }

    public void setClientPatienceTime(int clientPatienceTime) {
        entry.setClientPatienceTime(clientPatienceTime);
    }

}

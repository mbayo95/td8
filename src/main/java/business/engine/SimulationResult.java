package business.engine;

import java.util.ArrayList;
import java.util.List;

import business.model.AbstractClient;

/**
 * This class collects simulation statistic result to be persisted after.
 */
public class SimulationResult {
	private List<AbstractClient> clients = new ArrayList<AbstractClient>();
	private int totalCashierOccupation = 0;

	public void registerServedClient(AbstractClient client) {
		client.setServed(true);
		clients.add(client);
	}

	public void registerNonServedClient(AbstractClient client) {
		clients.add(client);
	}

	public void cashierOccupationRecord() {
		totalCashierOccupation++;
	}

	public List<AbstractClient> getClients() {
		return clients;
	}

	public int getTotalCashierOccupation() {
		return totalCashierOccupation;
	}

	public void setClients(List<AbstractClient> clients) {
		this.clients = clients;
	}

	public void setTotalCashierOccupation(int totalCashierOccupation) {
		this.totalCashierOccupation = totalCashierOccupation;
	}

}

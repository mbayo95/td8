package business.engine;

import java.util.List;

import dao.DAOSpring;
import dao.StatisticDAO;
import business.model.AbstractClient;
import business.model.SimulationEntry;

public class StatisticCalculator {
	private SimulationEntry entry;
	private SimulationResult result;

	private StatisticDAO dao = (StatisticDAO) DAOSpring.getBean("hibernateDAO");

	public void retrieveStatisticResult(int id) {
		entry = dao.getSimulationEntry(id);
		result = dao.getSimulationResult(id);
	}

	public double calculateAverageCashierOccupationRate() {
		System.out.println("Total : " + result.getTotalCashierOccupation());
		return (result.getTotalCashierOccupation() * 100 / entry.getSimulationDuration()) / entry.getCashierCount();
	}

	public double calculateAverageClientWaitingTime() {
		int totalWaitingTime = 0;
		int servedClientCount = 0;
		List<AbstractClient> clients = result.getClients();
		for (AbstractClient client : clients) {
			if (client.isServed()) {
				servedClientCount++;
				int serviceStartTime = client.getServiceStartTime();
				int arrivalTime = client.getArrivalTime();
				totalWaitingTime += serviceStartTime - arrivalTime;
			}
		}

		return totalWaitingTime / servedClientCount;
	}

	public double calculateAverageClientServiceTime() {
		int totalServiceTime = 0;
		int servedClientCount = 0;
		List<AbstractClient> clients = result.getClients();
		for (AbstractClient client : clients) {
			if (client.isServed()) {
				servedClientCount++;
				int departureTime = client.getDepartureTime();
				int serviceStartTime = client.getServiceStartTime();
				totalServiceTime += departureTime - serviceStartTime;
			}
		}
		return totalServiceTime / servedClientCount;
	}

	public int servedClientCount() {
		int servedClientCount = 0;
		List<AbstractClient> clients = result.getClients();
		for (AbstractClient client : clients) {
			if (client.isServed()) {
				servedClientCount++;
			}
		}
		return servedClientCount;
	}

	public int nonServedClientCount() {
		return result.getClients().size() - servedClientCount();
	}

	public double calculateClientSatisfactionRate() {
		return servedClientCount() * 100 / result.getClients().size();
	}
}

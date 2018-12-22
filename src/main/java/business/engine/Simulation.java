package business.engine;

import java.util.List;

import business.model.AbstractClient;
import business.model.AbstractOperation;
import business.model.SimulationEntry;
import dao.DAOSpring;
import dao.StatisticDAO;

/**
 * System simulation.
 * 
 * Access point of all related information.
 * 
 * This is a Spring managed bean, so the getters/setters and default constructor
 * are necessary.
 */
public class Simulation {
	private Bank bank;
	private SimulationResult result;
	private SimulationEntry entry;

	public Simulation() {
	}

	/**
	 * Main business logic : simulation algorithm.
	 * 
	 * This algorithm can be described by a UML sequence diagram.
	 */
	public int simulate() {
		buildBank();

		int simulationDuration = entry.getSimulationDuration();
		int clientArrivalInterval = entry.getClientArrivalInterval();

		for (int currentSystemTime = 0; currentSystemTime <= simulationDuration; currentSystemTime++) {

//			SimulationUtility.printBankStat(currentSystemTime, bank);

			// Before serving potential new clients, update bank existing
			// elements (cashier, queue, etc.)
			updateBank(currentSystemTime);

			boolean newClientArrival = SimulationUtility.newClientArrival(clientArrivalInterval, currentSystemTime);
			if (newClientArrival) {
				int serviceTime = SimulationUtility.generateRandomServiceTime(entry);
				double priorityClientRate = entry.getPriorityClientRate();
				AbstractClient client = SimulationUtility.getRandomClient(priorityClientRate);
				client.setArrivalTime(currentSystemTime);
				int clientPatienceTime = entry.getClientPatienceTime();
				client.setPatienceTime(clientPatienceTime);
				AbstractOperation operation = client.getOperation();
				operation.setServiceTime(serviceTime);

				Cashier freeCashier = bank.getFreeCashier();
				if (freeCashier == null) {
					SimulationUtility.printClientArrival(currentSystemTime, false);
					Queue queue = bank.getQueue();
					queue.addQueueLast(client);
				} else {
					SimulationUtility.printClientArrival(currentSystemTime, true);
					serveClient(currentSystemTime, freeCashier, client);
				}
			}
		}

		return persistSimulationResult();
	}

	private void updateBank(int currentSystemTime) {
		List<Cashier> cashiers = bank.getCashiers();
		Queue queue = bank.getQueue();
		for (Cashier cashier : cashiers) {

			if (!cashier.isFree()) {
				result.cashierOccupationRecord();
			}

			cashier.work();

			if (cashier.serviceFinished()) {
				// Leaving client
				AbstractClient leavingClient = cashier.getServingClient();
				leavingClient.setDepartureTime(currentSystemTime);
				SimulationUtility.printClientDeparture(currentSystemTime);
				result.registerServedClient(leavingClient);

				// Free the cashier
				cashier.setServingClient(null);

				// Serve a client in the queue
				if (!queue.isEmpty()) {
					AbstractClient nextClient;

					nextClient = queue.findPriorityClient();
					if (nextClient == null) {
						nextClient = queue.getQueueFirst();
					} else {
						queue.removePriorityClient(nextClient);
					}
					serveClient(currentSystemTime, cashier, nextClient);
				}
			}
		}

		// Impatient clients leave
		queue.updateClientPatience();
		List<AbstractClient> impatientClients = queue.removeImpatientClients();
		for (AbstractClient client : impatientClients) {
			client.setDepartureTime(currentSystemTime);
			result.registerNonServedClient(client);
			SimulationUtility.printClientDepartureWithoutBeingServed(currentSystemTime);
		}
	}

	private void serveClient(int currentSystemTime, Cashier cashier, AbstractClient client) {
		client.setServiceStartTime(currentSystemTime);
		AbstractOperation operation = client.getOperation();
		int serviceTime = operation.getServiceTime();
		cashier.serve(client, serviceTime);
		SimulationUtility.printServiceTimeTrace(currentSystemTime, serviceTime);
	}

	private void buildBank() {
		int cashierCount = entry.getCashierCount();
		bank = new Bank(cashierCount);
		result = new SimulationResult();
		System.out.println("Result cleared up");
	}

	private int persistSimulationResult() {
		StatisticDAO dao = (StatisticDAO) DAOSpring.getBean("hibernateDAO");
		dao.initDataSource();
		return dao.persist(entry, result);
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public SimulationResult getResult() {
		return result;
	}

	public void setResult(SimulationResult result) {
		this.result = result;
	}

	public SimulationEntry getEntry() {
		return entry;
	}

	public void setEntry(SimulationEntry entry) {
		this.entry = entry;
	}

}

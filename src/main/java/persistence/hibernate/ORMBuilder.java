package persistence.hibernate;

import java.util.ArrayList;
import java.util.List;

import persistence.data.ClientData;
import persistence.data.OperationData;
import persistence.data.SimulationData;

import business.engine.SimulationResult;
import business.model.AbstractClient;
import business.model.AbstractOperation;
import business.model.Client;
import business.model.Consultation;
import business.model.SimulationEntry;
import business.model.Transfer;
import business.model.VIPClient;
import business.model.Withdraw;

/**
 * This class constructs persistence objects from business objects and vice
 * versa.
 * 
 */
public class ORMBuilder {
	public static SimulationData buildData(SimulationEntry entry, SimulationResult result) {
		List<ClientData> allClients = new ArrayList<ClientData>();
		for (AbstractClient client : result.getClients()) {
			AbstractOperation operation = client.getOperation();
			OperationData operationData = new OperationData(operation.getClass().getName(), operation.getServiceTime(), operation.isUrgent());
			ClientData clientData = new ClientData(client.getArrivalTime(), client.getServiceStartTime(), client.getDepartureTime(),
					client.isServed(), client.isPriority(), operationData);
			allClients.add(clientData);
		}

		return new SimulationData(entry.getSimulationDuration(), entry.getCashierCount(), entry.getMinServiceTime(), entry.getMaxServiceTime(),
				entry.getClientArrivalInterval(), entry.getPriorityClientRate(), entry.getClientPatienceTime(), result.getTotalCashierOccupation(),
				allClients);
	}

	public static SimulationEntry buildEntry(SimulationData data) {
		return new SimulationEntry(data.getSimulationDuration(), data.getCashierCount(), data.getMinServiceTime(), data.getMaxServiceTime(),
				data.getClientArrivalInterval(), data.getPriorityClientRate(), data.getClientPatienceTime());

	}

	public static SimulationResult buildResult(SimulationData data) {
		SimulationResult result = new SimulationResult();
		result.setTotalCashierOccupation(data.getTotalCashierOccupation());
		List<AbstractClient> clients = new ArrayList<AbstractClient>();
		List<ClientData> persistenceClients = data.getAllClients();
		for (ClientData clientData : persistenceClients) {
			AbstractOperation operation = buildOperation(clientData);
			AbstractClient client;
			if (!clientData.isPriority()) {
				client = new Client(clientData.getArrivalTime(), operation, data.getClientPatienceTime());
			} else {
				client = new VIPClient(clientData.getArrivalTime(), operation, data.getClientPatienceTime());
			}
			client.setDepartureTime(clientData.getDepartureTime());
			client.setServed(clientData.isServed());
			client.setServiceStartTime(clientData.getServiceStartTime());

			clients.add(client);
		}

		result.setClients(clients);

		return result;
	}

	private static AbstractOperation buildOperation(ClientData clientData) {
		AbstractOperation operation = null;
		String operationName = clientData.getOperation().getName();
		int serviceTime = clientData.getOperation().getServiceTime();
		if (operationName.equals(Consultation.class.getName())) {
			operation = new Consultation(serviceTime);
		} else if (operationName.equals(Transfer.class.getName())) {
			operation = new Transfer(serviceTime);
		} else {
			operation = new Withdraw(serviceTime);
		}
		return operation;
	}
}

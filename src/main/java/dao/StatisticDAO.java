package dao;

import java.util.List;

import business.engine.SimulationResult;
import business.model.SimulationEntry;

/**
 * DAO interface used by business layer.
 */
public interface StatisticDAO {

	int persist(SimulationEntry entry, SimulationResult result);

	List<Integer> getAllSimulationEntryIds();

	SimulationEntry getSimulationEntry(int id);

	SimulationResult getSimulationResult(int id);
	
	/**
	 * Dangerous method that clears and reconstructs the data source used by persistence work.
	 */
	void initDataSource();

}

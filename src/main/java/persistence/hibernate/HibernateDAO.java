package persistence.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import persistence.data.SimulationData;

import dao.StatisticDAO;

import business.engine.SimulationResult;
import business.model.SimulationEntry;

public class HibernateDAO implements StatisticDAO {

	@Override
	public int persist(SimulationEntry entry, SimulationResult result) {
		Session session = HibernateConnection.getSession();
		Transaction transaction = session.beginTransaction();
		SimulationData data = ORMBuilder.buildData(entry, result);

		Serializable id = session.save(data);
		transaction.commit();

		session.close();
		return (Integer) id;
	}

	@Override
	public List<Integer> getAllSimulationEntryIds() {
		Session session = HibernateConnection.getSession();
		Transaction readTransaction = session.beginTransaction();
		Query readQuery = session.createQuery("from SimulationData");
		List<?> resultList = readQuery.list();
		List<Integer> ids = new ArrayList<Integer>();
		for (Object obj : resultList) {
			SimulationData data = (SimulationData) obj;
			ids.add(data.getId());
		}
		readTransaction.commit();
		session.close();

		return ids;
	}

	@Override
	public SimulationEntry getSimulationEntry(int id) {
		Session session = HibernateConnection.getSession();
		Transaction readTransaction = session.beginTransaction();
		Query readQuery = session.createQuery("from SimulationData sd where sd.id = :id");
		readQuery.setInteger("id", id);
		List<?> result = readQuery.list();
		SimulationData data = (SimulationData) result.get(0);

		SimulationEntry simulationEntry = ORMBuilder.buildEntry(data);
		readTransaction.commit();
		session.close();

		return simulationEntry;
	}

	@Override
	public SimulationResult getSimulationResult(int id) {
		Session session = HibernateConnection.getSession();
		Transaction readTransaction = session.beginTransaction();
		Query readQuery = session.createQuery("from SimulationData sd where sd.id = :id");
		readQuery.setInteger("id", id);
		List<?> result = readQuery.list();
		SimulationData data = (SimulationData) result.get(0);
		SimulationResult simulationResult = ORMBuilder.buildResult(data);
		readTransaction.commit();
		session.close();

		return simulationResult;
	}

	@Override
	public void initDataSource() {
		HiberanteDBInit.createTables();
	}
}

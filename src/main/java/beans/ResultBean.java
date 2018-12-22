package beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import business.BusinessSpring;
import business.engine.StatisticCalculator;

@ManagedBean
@RequestScoped
public class ResultBean {

	@ManagedProperty(value = "#{entryBean}")
	private EntryBean entryBean;

	private StatisticCalculator calculator = (StatisticCalculator) BusinessSpring.getBean("calculator");

	public ResultBean() {
		
	}

	public int getServedClientCount() {
		return calculator.servedClientCount();
	}

	public int getNonServedClientCount() {
		return calculator.nonServedClientCount();
	}
	
	public double getAverageClientWaitingTime() {
		return calculator.calculateAverageClientWaitingTime();
	}
	
	public double getAverageClientServiceTime() {
		return calculator.calculateAverageClientServiceTime();
	}
	
	public double getCashierOccupationRate() {
		return calculator.calculateAverageCashierOccupationRate();
	}
	
	public double getClientSatisfactionRate() {
		return calculator.calculateClientSatisfactionRate();
	}

	public EntryBean getEntryBean() {
		return entryBean;
	}

	public void setEntryBean(EntryBean entryBean) {
		this.entryBean = entryBean;
		calculator.retrieveStatisticResult(entryBean.getId());
	}

	public StatisticCalculator getCalculator() {
		return calculator;
	}

	public void setCalculator(StatisticCalculator calculator) {
		this.calculator = calculator;
	}

}

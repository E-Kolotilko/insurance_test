package kolotilko.e.insurance_test.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="CONTRACTS")
public class ContractShort {

    @Id
    @Column(name=Contract.CONTRACT_NUMBER_SQL_KEY, nullable=false)    
    long contractNumber;
    
    @Column(name=Contract.CONTRACT_DATE_SQL_KEY, nullable=true)
    Date contractDate;
    
    @Column(name=Contract.AWARD_SQL_KEY, nullable=false)
    String award;

    @Column(name=Contract.ACTIVE_FROM_SQL_KEY, nullable=false)
    Date activeFrom;
    
    @Column(name=Contract.ACTIVE_TO_SQL_KEY, nullable=false)
    Date activeTo;
    
    @ManyToOne(optional=false)
    @JoinColumn(name="CLIENT_ID",updatable=false,insertable=false)
    Client aClient;
    

    public String getFIO() {
        if (aClient!=null) {
            if (aClient.patronymic == null) {
                return aClient.surname+" "+aClient.name;
            }
            else {
                return aClient.surname+" "+aClient.name+" "+aClient.patronymic;
            }
        }
        else {
            return null;
        }
    }
    
    public long getContractNumber() {
        return contractNumber;
    }
    public void setContractNumber(long contractNumber) {
        this.contractNumber = contractNumber;
    }

    public Date getContractDate() {
        return contractDate;
    }
    public void setContractDate(Date contractDate) {
        this.contractDate = contractDate;
    }

    public String getAward() {
        return award;
    }
    public void setAward(String award) {
        this.award = award;
    }

    public Date getActiveFrom() {
        return activeFrom;
    }
    public void setActiveFrom(Date activeFrom) {
        this.activeFrom = activeFrom;
    }

    public Date getActiveTo() {
        return activeTo;
    }
    public void setActiveTo(Date activeTo) {
        this.activeTo = activeTo;
    }

    public Client getaClient() {
        return aClient;
    }
    public void setaClient(Client aClient) {
        this.aClient = aClient;
    }
    
}

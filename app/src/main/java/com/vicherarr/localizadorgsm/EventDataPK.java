package com.vicherarr.localizadorgsm;


import java.io.Serializable;


/**
 *
 * @author Víctor
 */
public class EventDataPK implements Serializable {
    private String accountID;
    private String deviceID;
    private int timestamp;
    private int statusCode;

    public EventDataPK() {
    }

    public EventDataPK(String accountID, String deviceID, int timestamp, int statusCode) {
        this.accountID = accountID;
        this.deviceID = deviceID;
        this.timestamp = timestamp;
        this.statusCode = statusCode;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (accountID != null ? accountID.hashCode() : 0);
        hash += (deviceID != null ? deviceID.hashCode() : 0);
        hash += (int) timestamp;
        hash += (int) statusCode;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EventDataPK)) {
            return false;
        }
        EventDataPK other = (EventDataPK) object;
        if ((this.accountID == null && other.accountID != null) || (this.accountID != null && !this.accountID.equals(other.accountID))) {
            return false;
        }
        if ((this.deviceID == null && other.deviceID != null) || (this.deviceID != null && !this.deviceID.equals(other.deviceID))) {
            return false;
        }
        if (this.timestamp != other.timestamp) {
            return false;
        }
        if (this.statusCode != other.statusCode) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "clienterest.EventDataPK[ accountID=" + accountID + ", deviceID=" + deviceID + ", timestamp=" + timestamp + ", statusCode=" + statusCode + " ]";
    }
    
}


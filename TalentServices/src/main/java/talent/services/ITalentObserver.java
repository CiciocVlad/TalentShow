package talent.services;

import talent.model.Result;

import java.rmi.RemoteException;
import java.rmi.Remote;

public interface ITalentObserver extends Remote {
    void newResultAdded(Result result) throws RemoteException, TalentException;
}

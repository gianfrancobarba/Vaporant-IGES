package it.unisa.model;

import java.sql.SQLException;
import java.util.ArrayList;

import com.google.gson.Gson;

public class AddressList {
    
    private static UserDaoImpl userDao = new UserDaoImpl();
    private static AddressDaoImpl addressDao = new AddressDaoImpl();
    private ArrayList<AddressBean> addressList;
    private ArrayList<AddressScript> addressListScript;
    
    public AddressList(UserBean user) {
        try {
            // Verifica se l'utente esiste nel database
            user = userDao.findByCred(user.getEmail(), user.getPassword());
            
            if (user != null) {
                setAddressList(new ArrayList<AddressBean>());
                addressListScript = new ArrayList<AddressScript>();

                // Verifica se la lista di indirizzi è valida
                ArrayList<AddressBean> userAddressList = addressDao.findByID(user.getId());
                
                if (userAddressList != null) {
                    setAddressList(userAddressList);

                    for (int i = 0; i < addressList.size(); i++) {
                        addressListScript.add(new AddressScript(addressList.get(i)));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void removeAddress(AddressBean address) {
        if (addressList != null) {
            addressList.remove(address);
            try {
                addressDao.deleteAddress(address);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<AddressBean> getAddressList() {
        return addressList;
    }

    public void setAddressList(ArrayList<AddressBean> addressList) {
        this.addressList = addressList;
    }

    public ArrayList<AddressScript> getAddressListScript() {
        return addressListScript;
    } 
    
    public String getJson() {
        if (addressListScript != null) {
            Gson gson = new Gson();
            String json = gson.toJson(addressListScript);
            return json;
        }
        return ""; // Ritorna una stringa vuota se la lista è nulla
    }
}
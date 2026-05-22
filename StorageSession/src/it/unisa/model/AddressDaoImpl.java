package it.unisa.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class AddressDaoImpl implements AddressDAO {
	private static final String TABLE = "indirizzo";
	private static DataSource ds;

    
	//connessione al database
	static {
	    try {
	        Context initCtx = new InitialContext();
	        Context envCtx = (Context) initCtx.lookup("java:comp/env");
	
	        ds = (DataSource) envCtx.lookup("jdbc/storage");
	
	    } catch (NamingException e) {
	        System.out.println("Error:" + e.getMessage());
	    }
	}
    
	@Override
	public int saveAddress(AddressBean address) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int result;

        String insertSQL = "INSERT INTO " + AddressDaoImpl.TABLE
                           + " (ID_Utente, stato, citta, via, numCivico, cap, provincia)"
                           + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
        	connection = ds.getConnection();
        	preparedStatement = connection.prepareStatement(insertSQL);

            preparedStatement.setInt(1, address.getId_utente());
            preparedStatement.setString(2, address.getStato());
            preparedStatement.setString(3, address.getCitta());
            preparedStatement.setString(4, address.getVia());
            preparedStatement.setString(5, address.getNumCivico());
            preparedStatement.setString(6, address.getCap());
            preparedStatement.setString(7, address.getProvincia());


            result = preparedStatement.executeUpdate();

        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
                
            } finally {
            	if(connection != null) {
            		connection.close();
            	}
            }
        }
        
        return result;
	}


	@Override
	public int deleteAddress(AddressBean address) throws SQLException {
		
		
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        
        String selectSQL = "DELETE FROM " + TABLE + " WHERE ID = ?";
        
        int result;
        
        try
        {
        	connection = ds.getConnection();
        	preparedStatement = connection.prepareStatement(selectSQL);
            
            preparedStatement.setInt(1, address.getId());
            
            System.out.println(preparedStatement);
            
            result = preparedStatement.executeUpdate();   
            
            connection.commit();
        	
        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
            } finally {
            	if(connection != null) {
            		connection.close();
            	}
            }
        }
        
        return result;
	}

	@Override
	public AddressBean findByCred(String cap, String via, String numCivico) throws SQLException {
		
		Connection connection = null;
        PreparedStatement preparedStatement = null;

        String selectSQL = "SELECT * FROM " + TABLE + " WHERE cap = ? AND via = ? AND numCivico = ?";
        AddressBean address = null;

        try {
//            connection = DriverManagerConnectionPool.getConnection();
        	connection = ds.getConnection();
        	preparedStatement = connection.prepareStatement(selectSQL);
            
            preparedStatement.setString(1, cap);
            preparedStatement.setString(2, via);
            preparedStatement.setString(3, numCivico);
            
            ResultSet rs = preparedStatement.executeQuery();
            if(!rs.isBeforeFirst()) return null;
            
            address = new AddressBean();
           
           while (rs.next()) {
        	   	address.setId(rs.getInt("ID"));
                address.setCap(rs.getString("cap"));
                address.setCitta(rs.getString("citta"));
                address.setId(rs.getInt("ID"));
                address.setId_utente(rs.getInt("ID_Utente"));
                address.setNumCivico(rs.getString("numCivico"));
                address.setProvincia(rs.getString("provincia"));
                address.setStato(rs.getString("stato"));
                address.setVia(rs.getString("via"));
               
            }
            
        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
            } finally {
            	if(connection != null) {
            		connection.close();
            	}
//                DriverManagerConnectionPool.releaseConnection(connection);
            }
        }
        
        return address;
	}


	@Override
    public ArrayList<AddressBean> findByID(int id) throws SQLException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ArrayList<AddressBean> ListaIndirizzi = new ArrayList<AddressBean>();

        String selectSQL = "SELECT * FROM "+ TABLE + " WHERE ID_Utente = ?";


        try {
            connection = DriverManagerConnectionPool.getConnection();
            preparedStatement = connection.prepareStatement(selectSQL);

            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();

            if(!rs.isBeforeFirst()) return null;



            while (rs.next()) {
                AddressBean address = new AddressBean();
                address.setId(rs.getInt("ID"));
                address.setCap(rs.getString("cap"));
                address.setCitta(rs.getString("citta"));
                address.setId(rs.getInt("ID"));
                address.setId_utente(rs.getInt("ID_Utente"));
                address.setNumCivico(rs.getString("numCivico"));
                address.setProvincia(rs.getString("provincia"));
                address.setStato(rs.getString("stato"));
                address.setVia(rs.getString("via"));
                ListaIndirizzi.add(address);
                }

      } finally {
          try {
              if (preparedStatement != null)
                  preparedStatement.close();
          } finally {
              DriverManagerConnectionPool.releaseConnection(connection);
          }
      }

      return ListaIndirizzi;
    }
    public AddressBean findAddressByID(int id) throws SQLException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        AddressBean address = new AddressBean();

        String selectSQL = "SELECT * FROM "+ TABLE + " WHERE ID = ?";


        try {
            connection = DriverManagerConnectionPool.getConnection();
            preparedStatement = connection.prepareStatement(selectSQL);

            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();

            if(!rs.isBeforeFirst()) return null;



            while (rs.next()) {
              
                address = new AddressBean();
                address.setId(rs.getInt("ID"));
                address.setCap(rs.getString("cap"));
                address.setCitta(rs.getString("citta"));
                address.setId(rs.getInt("ID"));
                address.setId_utente(rs.getInt("ID_Utente"));
                address.setNumCivico(rs.getString("numCivico"));
                address.setProvincia(rs.getString("provincia"));
                address.setStato(rs.getString("stato"));
                address.setVia(rs.getString("via"));
                }

      } finally {
          try {
              if (preparedStatement != null)
                  preparedStatement.close();
          } finally {
              DriverManagerConnectionPool.releaseConnection(connection);
          }
      }

      return address;
    }
	
}

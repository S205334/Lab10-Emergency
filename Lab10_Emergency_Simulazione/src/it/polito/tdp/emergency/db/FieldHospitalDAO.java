////////////////////////////////////////////////////////////////////////////////
//             //                                                             //
//   #####     // Field hospital simulator                                    //
//  ######     // (!) 2013 Giovanni Squillero <giovanni.squillero@polito.it>  //
//  ###   \    //                                                             //
//   ##G  c\   // Field Hospital DAO                                          //
//   #     _\  // Test with MariaDB 10 on win                                 //
//   |   _/    //                                                             //
//   |  _/     //                                                             //
//             // 03FYZ - Tecniche di programmazione 2012-13                  //
////////////////////////////////////////////////////////////////////////////////

package it.polito.tdp.emergency.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import it.polito.tdp.emergency.simulation.Core;
import it.polito.tdp.emergency.simulation.Evento;
import it.polito.tdp.emergency.simulation.Paziente;



public class FieldHospitalDAO {

	@SuppressWarnings("deprecation")
	public void getAllConnection(Core simu) {
		
		String sql = "SELECT timestamp, id, name, triage "
				+ "FROM arrivals INNER JOIN patients ON arrivals.patient = patients.id ORDER BY timestamp ";
		
		try {
			Connection conn = DBConnect.getConnection();
			Statement st = conn.createStatement();
			ResultSet res = st.executeQuery(sql);
			
			while(res.next()) {
				simu.aggiungiPaziente(new Paziente(res.getInt("id"), res.getString("name"), Enum.valueOf(Paziente.StatoPaziente.class, res.getString("triage").toUpperCase())));
				Timestamp time = res.getTimestamp("timestamp");
				int minute = (int) (time.getDate()-25)*24*60+time.getHours()*60+time.getMinutes();
				simu.aggiungiEvento(new Evento(minute, Evento.TipoEvento.PAZIENTE_ARRIVA, res.getInt("id")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	// TODO
}

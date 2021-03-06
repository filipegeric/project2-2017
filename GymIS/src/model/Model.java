package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedList;

import connection.DBConnector;
import domen.Member;
import domen.Timestamp;

public class Model {

	private static DBConnector connector = new DBConnector();

	/**
	 * Metoda vraca sve clanove iz baze podataka.
	 * 
	 * @return lista objekata {@link Member}
	 * @throws SQLException
	 */
	public LinkedList<Member> getAllMembers() throws SQLException {
		LinkedList<Member> tempMembers = new LinkedList<Member>();
		Connection con = connector.connect();
		String query = "SELECT * FROM Members";

		PreparedStatement ps = con.prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			Member m = new Member();
			int i = 0;
			m.setId(rs.getInt(++i));
			m.setFirstName(rs.getString(++i));
			m.setLastName(rs.getString(++i));
			m.setGender(rs.getString(++i).charAt(0));
			m.setBirthdate(rs.getDate(++i));
			m.setPhoneNumber(rs.getString(++i));
			m.setStartDate(rs.getDate(++i));
			m.setEndDate(rs.getDate(++i));
			m.setHeight(rs.getDouble(++i));
			m.setWeight(rs.getDouble(++i));

			tempMembers.add(m);
		}
		rs.close();
		ps.close();
		con.close();
		return tempMembers;
	}

	/**
	 * Metoda za dodavanje clana u bazu podataka.
	 * 
	 * @param m
	 *            - objekat klase {@link Member}
	 * @throws SQLException
	 */
	public void addNewMember(Member m) throws SQLException {
		Connection con = connector.connect();
		String query = "INSERT INTO "
				+ "Members(firstName, lastName, gender, birthdate, phoneNumber, endDate, height, weight)"
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		PreparedStatement ps = con.prepareStatement(query);
		ps.setString(1, m.getFirstName());
		ps.setString(2, m.getLastName());
		ps.setString(3, String.valueOf(m.getGender()));
		ps.setDate(4, m.getBirthdate());
		ps.setString(5, m.getPhoneNumber());
		ps.setDate(6, m.getEndDate());
		ps.setDouble(7, m.getHeight());
		ps.setDouble(8, m.getWeight());

		ps.executeQuery();

		int maxIdEvidence = -1;
		int lastMemberIdMembers = -1;
		ResultSet rs;

		query = "SELECT MAX(membersId) FROM Evidence";
		ps = con.prepareStatement(query);
		rs = ps.executeQuery();
		rs.next();
		maxIdEvidence = rs.getInt(1);

		query = "SELECT MAX(id) FROM Members";
		ps = con.prepareStatement(query);
		rs = ps.executeQuery();
		rs.next();
		lastMemberIdMembers = rs.getInt(1);

		if (lastMemberIdMembers < maxIdEvidence) {
			query = "UPDATE Members SET id=? WHERE id=?";
			ps = con.prepareStatement(query);
			ps.setInt(1, maxIdEvidence + 1);
			ps.setInt(2, lastMemberIdMembers);
			ps.executeQuery();
		}

		ps.close();
		con.close();
	}

	/**
	 * Metoda za uklanjanje clana iz baze podataka.
	 * 
	 * @param id
	 *            - jedinstveni indetifikator clana.
	 * @return True ako je clan usmesno uklonjen.
	 * @throws SQLException
	 */
	public boolean removeMember(int id) throws SQLException {
		Connection con = connector.connect();
		String query = "DELETE FROM Members WHERE id=?";

		PreparedStatement ps = con.prepareStatement(query);
		ps.setInt(1, id);

		int temp = 0;
		temp = ps.executeUpdate();

		ps.close();
		con.close();
		return (temp != 0) ? true : false;

	}

	/**
	 * Metoda za pronalazenje clana.
	 * 
	 * @param id
	 *            - jedinstveni indetifikator clana.
	 * @return {@link Member} ako clan postoji u bazi.
	 * @throws SQLException
	 */
	public Member findMemberId(int id) throws SQLException {
		Member m = new Member();
		Connection con = connector.connect();
		String query = "SELECT * FROM Members WHERE id=?";

		PreparedStatement ps = con.prepareStatement(query);
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();

		boolean exist = rs.next();
		if (exist == false) {
			rs.close();
			ps.close();
			con.close();
			return null;
		}
		int i = 0;
		m.setId(rs.getInt(++i));
		m.setFirstName(rs.getString(++i));
		m.setLastName(rs.getString(++i));
		m.setGender(rs.getString(++i).charAt(0));
		m.setBirthdate(rs.getDate(++i));
		m.setPhoneNumber(rs.getString(++i));
		m.setStartDate(rs.getDate(++i));
		m.setEndDate(rs.getDate(++i));
		m.setHeight(rs.getDouble(++i));
		m.setWeight(rs.getDouble(++i));

		rs.close();
		ps.close();
		con.close();
		return m;
	}

	/**
	 * Metoda za pronalazenje svih clanova po imenu.
	 * 
	 * @param firstName
	 *            - Ime clana.
	 * @return Lista objekata {@link Member}
	 * @throws SQLException
	 */
	public LinkedList<Member> findMembersFirstName(String firstName) throws SQLException {
		LinkedList<Member> tempMemebers = new LinkedList<Member>();
		Connection con = connector.connect();
		String query = "SELECT * FROM Members WHERE firstName=?";

		PreparedStatement ps = con.prepareStatement(query);
		ps.setString(1, firstName);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			Member m = new Member();
			int i = 0;
			m.setId(rs.getInt(++i));
			m.setFirstName(rs.getString(++i));
			m.setLastName(rs.getString(++i));
			m.setGender(rs.getString(++i).charAt(0));
			m.setBirthdate(rs.getDate(++i));
			m.setPhoneNumber(rs.getString(++i));
			m.setStartDate(rs.getDate(++i));
			m.setEndDate(rs.getDate(++i));
			m.setHeight(rs.getDouble(++i));
			m.setWeight(rs.getDouble(++i));

			tempMemebers.add(m);
		}

		rs.close();
		ps.close();
		con.close();
		return tempMemebers;

	}

	/**
	 * Metoda za pronalazenje svih clanova po prezimenu.
	 * 
	 * @param firstName
	 *            - Prezime clana.
	 * @return Lista objekata {@link Member}
	 * @throws SQLException
	 */
	public LinkedList<Member> findMembersLastName(String lastName) throws SQLException {
		LinkedList<Member> tempMemebers = new LinkedList<Member>();
		Connection con = connector.connect();
		String query = "SELECT * FROM Members WHERE lastName=?";

		PreparedStatement ps = con.prepareStatement(query);
		ps.setString(1, lastName);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			Member m = new Member();
			int i = 0;
			m.setId(rs.getInt(++i));
			m.setFirstName(rs.getString(++i));
			m.setLastName(rs.getString(++i));
			m.setGender(rs.getString(++i).charAt(0));
			m.setBirthdate(rs.getDate(++i));
			m.setPhoneNumber(rs.getString(++i));
			m.setStartDate(rs.getDate(++i));
			m.setEndDate(rs.getDate(++i));
			m.setHeight(rs.getDouble(++i));
			m.setWeight(rs.getDouble(++i));

			tempMemebers.add(m);
		}

		rs.close();
		ps.close();
		con.close();
		return tempMemebers;

	}

	/**
	 * Metoda za uplatu clanarine clana.
	 * 
	 * @param id
	 *            - jedinstveni indetifikator clana.
	 * @param date
	 *            - datum do kog uplacuje clanarinu.
	 * @throws SQLException
	 */
	public void payMembership(int id, String date) throws SQLException {
		// date u formatu "yyyy-mm-dd"
		Connection con = connector.connect();
		String query = "UPDATE Members SET endDate=? WHERE id=?";

		PreparedStatement ps = con.prepareStatement(query);
		ps.setString(1, date);
		ps.setInt(2, id);

		ps.executeQuery();

		ps.close();
		con.close();
	}

	/**
	 * Metoda za izmenu podataka o clanu.
	 * 
	 * @param m
	 *            - objekat klaze {@link Member}
	 * @throws SQLException
	 */
	public void updateMember(Member m) throws SQLException {
		/*
		 * First we use findMemberId, method returns object Member, and we fill
		 * all TxtFields in GUI with that data then we change attributes we want
		 * to change in that Member object then we use updateMember, method
		 * UPDATEs all column, nevermind they are changed or not
		 */

		Connection con = connector.connect();
		String query = "UPDATE Members SET "
				+ "firstName=?, lastName=?, gender=?, birthDate=?, phoneNumber=?, endDate=?, height=?, weight=?"
				+ " WHERE id=?";

		PreparedStatement ps = con.prepareStatement(query);
		int i = 0;
		ps.setString(++i, m.getFirstName());
		ps.setString(++i, m.getLastName());
		ps.setString(++i, String.valueOf(m.getGender()));

		if (m.getBirthdate() != null)
			ps.setDate(++i, m.getBirthdate());
		else
			ps.setNull(++i, Types.DATE);

		if (m.getPhoneNumber() != null)
			ps.setString(++i, m.getPhoneNumber());
		else
			ps.setNull(++i, Types.VARCHAR);

		ps.setDate(++i, m.getEndDate());

		if (m.getHeight() != 0.0)
			ps.setDouble(++i, m.getHeight());// No need for if != null, default
												// value of double is 0.0
		else
			ps.setNull(++i, Types.DOUBLE);

		if (m.getWeight() != 0.0)
			ps.setDouble(++i, m.getWeight());
		else
			ps.setNull(++i, Types.DOUBLE);

		ps.setInt(++i, m.getId());

		ps.executeQuery();

		ps.close();
		con.close();

	}

	/**
	 * Metoda za evidenciju dolaska clana.
	 * 
	 * @param id
	 *            - Jedinstveni indetifikator clana.
	 * @throws SQLException
	 */
	public void enterRecord(int id) throws SQLException {
		Connection con = connector.connect();
		String query = "INSERT INTO Evidence(membersID) VALUES (?)";
		PreparedStatement ps = con.prepareStatement(query);
		ps.setInt(1, id);

		ps.executeQuery();

		ps.close();
		con.close();

	}

	/**
	 * Metoda za prijavu radnika.
	 * 
	 * @param username
	 *            - Korisnicko ime radnika.
	 * @param pass
	 *            - Lozinka radnika.
	 * @return True ako je radnik uspesno ulogovan.
	 * @throws SQLException
	 */
	public boolean logInGymWorker(String username, String pass) throws SQLException {
		Connection con = connector.connect();
		String query = "SELECT id FROM GymWorkers WHERE username=? AND passwrd=?";
		PreparedStatement ps = con.prepareStatement(query);
		ps.setString(1, username);
		ps.setString(2, pass);

		ResultSet rs = ps.executeQuery();
		rs.next();
		try {
			rs.getInt(1);
			return true;
		} catch (SQLDataException e) {
			return false;
		}
	}

	/**
	 * Metoda vraca listu objekata {@link Timestamp} koji predstavlja dolazak
	 * clana.
	 * 
	 * @param memId
	 *            - Jedinsrveni indetifikator clana.
	 * @return Listu objekata {@link Timestamp}
	 * @throws SQLException
	 */
	public LinkedList<Timestamp> getEvidenceOfMember(int memId) throws SQLException {
		LinkedList<Timestamp> tempLst = new LinkedList<Timestamp>();
		Connection con = connector.connect();
		String query = "SELECT id,timeStmp FROM Evidence WHERE membersId=?";
		PreparedStatement ps = con.prepareStatement(query);
		ps.setInt(1, memId);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			Timestamp tempTimeStamp = new Timestamp();

			tempTimeStamp.setId(rs.getInt(1));
			tempTimeStamp.setDate(rs.getDate(2));
			tempTimeStamp.setTime(rs.getTime(2));

			tempLst.add(tempTimeStamp);
		}

		rs.close();
		ps.close();
		con.close();
		return tempLst;

	}

}

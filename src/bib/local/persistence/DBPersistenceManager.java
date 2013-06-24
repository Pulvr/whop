package bib.local.persistence;

import java.io.IOException;

import bib.local.valueobjects.Person;
import bib.local.valueobjects.Ware;

public class DBPersistenceManager implements PersistenceManager {

	@Override
	public boolean close() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Ware ladeWare() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void openForReading(String datenquelle) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void openForWriting(String datenquelle) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean speichereWare(Ware w) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	

	@Override
	public Person ladePerson() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean speicherePerson(Person p) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	
}

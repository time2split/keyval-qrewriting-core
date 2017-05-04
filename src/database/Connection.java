package database;

import java.io.Closeable;
import java.util.ArrayList;

import json.Json;

public abstract class Connection implements Closeable
{
	abstract public ArrayList<Json> find(Json query);

	abstract public void useDatabase(String database);
}

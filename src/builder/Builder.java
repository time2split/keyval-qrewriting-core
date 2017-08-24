package builder;

/**
 * Permet de construire/ajouter des informations à un objet
 * 
 * @author zuri
 * 
 */
abstract public class Builder
{
	/**
	 * Objet à construire
	 */
	private Object	builded;

	public Builder()
	{
	}

	protected Builder(Object d)
	{
		setBuilded(d);
	}

	protected void setBuilded(Object d)
	{
		builded = d;
	}

	protected Object getBuilded()
	{
		return builded;
	}

	/**
	 * Construit l'objet enregistré ($builded)
	 * 
	 * @throws BuilderException
	 */
	abstract public void build() throws BuilderException;

	/**
	 * @return Un nouvel objet construit
	 */
	abstract public Object newBuild() throws BuilderException;
}
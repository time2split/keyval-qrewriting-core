package insomnia.qrewriting.query;

public interface LabelFactory
{
	public Label from(String label);
	public Label from(String[] label);
	public Label from(Label label);
	public Label emptyLabel();
}

package builder;

import factory.Factory;

public abstract class BuilderDataFactory extends Factory
{
	@Override
	abstract public BuilderData create();

}

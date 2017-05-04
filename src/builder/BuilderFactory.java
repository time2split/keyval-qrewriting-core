package builder;

import factory.Factory;

public abstract class BuilderFactory extends Factory
{
	@Override
	abstract public Builder create();
}

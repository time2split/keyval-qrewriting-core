package insomnia.qrewritingnorl1.query_building.mongo;

import insomnia.builder.BuilderDataFactory;

public class FactoryOfJsonBuilder_query extends BuilderDataFactory
{

	@Override
	public JsonBuilder_query create()
	{
		return new JsonBuilder_query();
	}
}

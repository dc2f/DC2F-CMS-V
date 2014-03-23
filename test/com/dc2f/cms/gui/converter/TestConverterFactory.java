package com.dc2f.cms.gui.converter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.ReverseConverter;

public class TestConverterFactory {
	
	private static ConverterFactory factory;

	@Before
	public void setup() {
		factory = new ConverterFactory();
		factory.register(new AtoBConverter());
		factory.register(new BtoDConverter());
	}
	
	@Test
	public void testSimplePath() {
		Converter<A, B> aToB = factory.createConverter(A.class, B.class);
		assertNotNull("Converter Factory failed to find direct path from a to b", aToB);
		assertSame("Converter Factory failed to find direct path from a to b", A.class, aToB.getModelType());
		assertSame("Converter Factory failed to find direct path from a to b", B.class, aToB.getPresentationType());
	}
	
	@Test
	public void testReverseSimplePath() {
		Converter<B, A> bToA = factory.createConverter(B.class, A.class);
		assertNotNull("Converter Factory failed to find direct path from b to a with ReverseConverter", bToA);
		assertSame("Converter Factory failed to find direct path from b to a with ReverseConverter", B.class, bToA.getModelType());
		assertSame("Converter Factory failed to find direct path from b to a with ReverseCOnverter", A.class, bToA.getPresentationType());
		assertTrue("Converter Factory failed to find direct path from b to a with ReverseCOnverter", bToA instanceof ReverseConverter);
	}
	
	@Test
	public void testPathWithSuperClass(){
		//this test shouldn't find any path as it would be impossible to convert an instance of C into D and converters must go both ways
		Converter<A, C> aToC = factory.createConverter(A.class, C.class);
		assertNull("Converter Factory shouldn't find direct path from a to c with super class", aToC);
	}
	
	@Test
	public void testIndirectPath() {
		Converter<A, D> aToD = factory.createConverter(A.class, D.class);
		assertNotNull("Converter Factory failed to find direct path from a to d with indirect path", aToD);
		assertSame("Converter Factory failed to find direct path from a to d with indirect path", A.class, aToD.getModelType());
		assertSame("Converter Factory failed to find direct path from a to d with indirect path", D.class, aToD.getPresentationType());
	}
	
	@Test
	public void testImpossiblePath() {
		Converter<A, E> aToE = factory.createConverter(A.class, E.class);
		assertNull("Converter Factory should not return a converter for a class that has no valid paths.", aToE);	
	}
	
	private static class A {
		
	}
	
	private static class B {
		
	}
	
	private static class C {
		
	}
	
	private static class D extends C {
		
	}
	
	private static class E {
		
	}
	
	private static class AtoBConverter implements Converter<B, A> {

		@Override
		public A convertToModel(B value, Class<? extends A> targetType,
				Locale locale)
				throws com.vaadin.data.util.converter.Converter.ConversionException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public B convertToPresentation(A value, Class<? extends B> targetType,
				Locale locale)
				throws com.vaadin.data.util.converter.Converter.ConversionException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Class<A> getModelType() {
			return A.class;
		}

		@Override
		public Class<B> getPresentationType() {
			return B.class;
		}

	}
	
	private static class BtoDConverter implements Converter<D, B> {

		@Override
		public B convertToModel(D value, Class<? extends B> targetType,
				Locale locale)
				throws com.vaadin.data.util.converter.Converter.ConversionException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public D convertToPresentation(B value, Class<? extends D> targetType,
				Locale locale)
				throws com.vaadin.data.util.converter.Converter.ConversionException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Class<B> getModelType() {
			return B.class;
		}

		@Override
		public Class<D> getPresentationType() {
			return D.class;
		}
		
	}
}

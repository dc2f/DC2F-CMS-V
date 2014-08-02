package com.dc2f.cms.gui.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
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
		factory.register(new BtoAConverter());
		factory.register(new DtoBConverter());
	}
	
	@Test
	public void testSimplePath() {
		Converter<B, A> bToA = factory.createConverter(B.class, A.class);
		assertNotNull("Converter Factory failed to find direct path from b to a", bToA);
		assertSame("Converter Factory failed to find direct path from b to a", B.class, bToA.getPresentationType());
		assertSame("Converter Factory failed to find direct path from b to a", A.class, bToA.getModelType());
	}
	
	@Test
	public void testReverseSimplePath() {
		Converter<A, B> aToB = factory.createConverter(A.class, B.class);
		assertNotNull("Converter Factory failed to find direct path from a to b with ReverseConverter", aToB);
		assertSame("Converter Factory failed to find direct path from a to b with ReverseConverter", A.class, aToB.getPresentationType());
		assertSame("Converter Factory failed to find direct path from a to b with ReverseConverter", B.class, aToB.getModelType());
		assertTrue("Converter Factory failed to find direct path from a to b with ReverseConverter", aToB instanceof ReverseConverter);
	}
	
	@Test
	public void testReverseConversion() {
		factory.clear();
		factory.register(new StringToClassConverter());
		@SuppressWarnings("rawtypes")
		Converter<String, Class> stringToClass = factory.createConverter(String.class, Class.class);
		Class<?> clazz = stringToClass.convertToModel("com.dc2f.cms.gui.converter.TestConverterFactory", Class.class, null);
		assertSame("conversion from string to class via reverse converter didn't work with the factory.", TestConverterFactory.class, clazz);
		
	}
	
	@Test
	public void testReverseConverter() {
		StringToClassConverter converter = new StringToClassConverter();
		assertSame(TestConverterFactory.class, converter.convertToModel("com.dc2f.cms.gui.converter.TestConverterFactory", Class.class, null));
		assertEquals("com.dc2f.cms.gui.converter.TestConverterFactory", converter.convertToPresentation(TestConverterFactory.class, String.class, null));
		assertEquals("com.dc2f.cms.gui.converter.TestConverterFactory", new ReverseConverter<>(converter).convertToModel(TestConverterFactory.class, String.class, null));
		assertSame(TestConverterFactory.class, new ReverseConverter<>(converter).convertToPresentation("com.dc2f.cms.gui.converter.TestConverterFactory", Class.class, null));
	}
	
	@Test
	public void testPathWithSuperClass(){
		//this test shouldn't find any path as it would be impossible to convert an instance of C into D and converters must go both ways
		Converter<A, C> aToC = factory.createConverter(A.class, C.class);
		assertNull("Converter Factory shouldn't find direct path from a to c with super class", aToC);
	}
	
	@Test
	public void testIndirectReversePath() {
		Converter<A, D> aToD = factory.createConverter(A.class, D.class);
		assertNotNull("Converter Factory failed to find direct path from a to d with indirect path", aToD);
		assertSame("Converter Factory failed to find direct path from a to d with indirect path", A.class, aToD.getPresentationType());
		assertSame("Converter Factory failed to find direct path from a to d with indirect path", D.class, aToD.getModelType());
	}
	
	@Test
	public void testIndirectPath() {
		Converter<D, A> dToA = factory.createConverter(D.class, A.class);
		assertNotNull("Converter Factory failed to find direct path from d to a with indirect path", dToA);
		assertSame("Converter Factory failed to find direct path from d to a with indirect path", D.class, dToA.getPresentationType());
		assertSame("Converter Factory failed to find direct path from d to a with indirect path", A.class, dToA.getModelType());
	}
	
	@Test
	public void testImpossiblePath() {
		Converter<A, E> aToE = factory.createConverter(A.class, E.class);
		assertNull("Converter Factory should not return a converter for a class that has no valid paths.", aToE);	
	}
	
	@Test
	public void testUselessConversion() {
		Converter<A, A> useless = factory.createConverter(A.class, A.class);
		assertNull("There shouldn't be any converters delievered if no conversion is needed.", useless);
	}
	
	@Test
	public void testGuessingConverter() {
		assertNull("There should be no convertion from string to string", factory.guessConverterFromString("abcdef"));
		testGuessingFromString(Integer.class, "0");
		testGuessingFromString(Long.class, "0l");
		testGuessingFromString(Double.class, "0.0");
		testGuessingFromString(Float.class, "0.0f");
		testGuessingFromString(File.class, "C:\\");
		testGuessingFromString(File.class, "PATH\\to\\my\\file");
		testGuessingFromString(File.class, "C:\\PATH\\to\\my\\file");
		testGuessingFromString(File.class, "/PATH/to/my/file");
		testGuessingFromString(File.class, "PATH/to/my/file");
		testGuessingFromString(File.class, "../PATH/to/my/file");
		
		
	}
	
	private void testGuessingFromString(Class<?> clazz, String string) {
		assertNotNull("Couldn't guess " + clazz.getSimpleName() + " from string \"" + string + "\".", factory.guessConverterFromString(string));
		assertSame("Couldn't guess " + clazz.getSimpleName() + " from string \"" + string + "\" correctly.", clazz, factory.guessConverterFromString(string).getModelType());
	}
	
	@Test
	public void testAvoidingStringAsIntermediate() {
		assertNull("String should be avoided as intermediate, as it doesn't make much sense.", factory.createConverter(B.class, F.class));
	}
	

	private static class A { }
	
	private static class B { }
	
	private static class C { }
	
	private static class D extends C { }
	
	private static class E { }
	
	private static class F { }
	
	private static class BtoAConverter implements Converter<B, A> {

		@Override
		public A convertToModel(B value, Class<? extends A> targetType,
				Locale locale)
				throws com.vaadin.data.util.converter.Converter.ConversionException {
			return null;
		}

		@Override
		public B convertToPresentation(A value, Class<? extends B> targetType,
				Locale locale)
				throws com.vaadin.data.util.converter.Converter.ConversionException {
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
	
	private static class DtoBConverter implements Converter<D, B> {

		@Override
		public B convertToModel(D value, Class<? extends B> targetType,
				Locale locale)
				throws com.vaadin.data.util.converter.Converter.ConversionException {
			return null;
		}

		@Override
		public D convertToPresentation(B value, Class<? extends D> targetType,
				Locale locale)
				throws com.vaadin.data.util.converter.Converter.ConversionException {
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
	
	private static class StringToFConverter implements Converter<String, F> {

		@Override
		public F convertToModel(String value, Class<? extends F> targetType,
				Locale locale)
				throws com.vaadin.data.util.converter.Converter.ConversionException {
			return null;
		}

		@Override
		public String convertToPresentation(F value,
				Class<? extends String> targetType, Locale locale)
				throws com.vaadin.data.util.converter.Converter.ConversionException {
			return null;
		}

		@Override
		public Class<F> getModelType() {
			return F.class;
		}
		
		@Override
		public Class<String> getPresentationType() {
			return String.class;
		}
	}
	
	private static class StringToBConverter implements Converter<String, B> {

		@Override
		public B convertToModel(String value, Class<? extends B> targetType,
				Locale locale)
				throws com.vaadin.data.util.converter.Converter.ConversionException {
			return null;
		}

		@Override
		public String convertToPresentation(B value,
				Class<? extends String> targetType, Locale locale)
				throws com.vaadin.data.util.converter.Converter.ConversionException {
			return null;
		}

		@Override
		public Class<B> getModelType() {
			return B.class;
		}
		
		@Override
		public Class<String> getPresentationType() {
			return String.class;
		}
	}
}

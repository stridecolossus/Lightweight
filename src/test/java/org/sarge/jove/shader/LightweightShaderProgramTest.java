package org.sarge.jove.shader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sarge.jove.util.LightweightTestRunner;

@RunWith(LightweightTestRunner.class)
public class LightweightShaderProgramTest {
	private LightweightShaderProgram program;
	private Shader shader;

	@Before
	public void before() throws IOException {
		// Load a shader
		shader = new LightweightShader(Shader.Type.FRAGMENT, "uniform vec4 col; void main(void) { gl_FragColor = col; }");

		// Create program
		program = new LightweightShaderProgram(new Shader[]{ shader });
	}

	@After
	public void after() {
		if((program != null) && (program.getResourceID() > 0)) program.release();
		if((shader != null) && (shader.getResourceID() > 0)) shader.release();
	}

	@Test
	public void constructor() {
		assertEquals(true, program.getResourceID() > 0);
	}

	@Test
	public void getParameter() {
		// Check underlying parameter descriptors
		assertNotNull(program.getParameters());
		assertEquals(1, program.getParameters().length);
		final ParameterDescriptor desc = program.getParameters()[0];
		assertEquals("col", desc.getName());
		assertEquals(1, desc.getLength());
		assertEquals(0x8B52, desc.getType());
		assertEquals(0, desc.getLocation());

		// Check generated parameter
		final ShaderParameter p = program.getParameter("col");
		assertNotNull(p);
		assertEquals("col", p.getName());
		assertEquals(1, p.getLength());
		assertEquals(ParameterType.FLOAT_4, p.getType());
		assertEquals(0, p.getLocation());
		assertNotNull(p.getBuffer());
	}

	@Test
	public void lifecycle() {
		getParameter();
		program.activate();
		program.reset();
		program.release();
	}
}

package org.sarge.jove.shader;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sarge.jove.util.LightweightTestRunner;

@RunWith( LightweightTestRunner.class )
public class LightweightShaderTest {
	private LightweightShader shader;

	@Before
	public void before() throws ShaderException {
		shader = new LightweightShader( Shader.Type.VERTEX, "void main(void) { gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex; }" );
	}

	@Test
	public void shaderLifecycle() {
		assertEquals( true, shader.getResourceID() > 0 );
		shader.release();
	}

	@SuppressWarnings("unused")
	@Test( expected = ShaderException.class )
	public void invalidCode() throws ShaderException {
		new LightweightShader( Shader.Type.VERTEX, "cobblers" );
	}
}

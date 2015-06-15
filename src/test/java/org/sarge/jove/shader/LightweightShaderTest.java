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
	public void before() {
		shader = new LightweightShader( Shader.Type.VERTEX, "void main(void) { gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex; }" );
	}

	@Test
	public void shaderLifecycle() {
		assertEquals( true, shader.getResourceID() > 0 );
		shader.release();
	}

	@SuppressWarnings("unused")
	@Test( expected = RuntimeException.class )
	public void invalidCode() {
		new LightweightShader( Shader.Type.VERTEX, "cobblers" );
	}
}

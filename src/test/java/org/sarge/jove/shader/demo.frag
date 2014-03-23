// Pretend fragment shader for testing parameters

uniform float 		f;
uniform vec3  		vec;
uniform vec4  		col;
uniform sampler2D	texture;
uniform mat4		matrix;

void main()
{
	// Ensure all parameters are referenced or they will be removed by the compiler
	vec2 temp;
	temp.x = vec.x + f;
	temp.y = col.y + vec3( m4 ).y;
   	gl_FragColor = texture2D( texture, temp );
}

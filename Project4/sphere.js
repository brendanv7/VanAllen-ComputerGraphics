var canvas;
var gl;

var numTimesToSubdivide = 5;

var index = 0;

var pointsArray = [];
var normalsArray = [];


var near = -10;
var far = 10;
var radius = 1.5;
var theta  = 0.0;
var phi    = 0.0;
var dr = 5.0 * Math.PI/180.0;

var left   = -2.0;
var right  = 2.0;
var ytop   = 2.0;
var bottom = -2.0;

var va = vec4(0.0, 0.0, -1.0,1);
var vb = vec4(0.0, 0.942809, 0.333333, 1);
var vc = vec4(-0.816497, -0.471405, 0.333333, 1);
var vd = vec4(0.816497, -0.471405, 0.333333,1);

var lightPosition = vec4(1.0, 1.0, 1.0, 0.0 );
var lightAmbient  = vec4(0.2, 0.2, 0.2, 1.0 );
var lightDiffuse  = vec4( 1.0, 1.0, 1.0, 1.0 );
var lightSpecular = vec4( 1.0, 1.0, 1.0, 1.0 );

var materialAmbient = vec4( 1.0, 0.0, 1.0, 1.0 );
var materialDiffuse = vec4( 1.0, 0.8, 0.0, 1.0 );
var materialSpecular = vec4( 1.0, 0.8, 1.0, 1.0 );
var materialShininess = 100.0;

var ctm;
var ambientColor, diffuseColor, specularColor;

var modelViewMatrix, projectionMatrix;
var modelViewMatrixLoc, projectionMatrixLoc;
var eye;
var at = vec3(0.0, 0.0, 0.0);
var up = vec3(0.0, 1.0, 0.0);

var colorVec = vec4(1.0, 1.0, 1.0, 1.0 );
var chosenMap=1;

var program;

// Create the texture from the image
function configureTexture( image ) {
    texture = gl.createTexture();
    gl.bindTexture( gl.TEXTURE_2D, texture );
    gl.pixelStorei(gl.UNPACK_FLIP_Y_WEBGL, true);
    gl.texImage2D( gl.TEXTURE_2D, 0, gl.RGB,
         gl.RGB, gl.UNSIGNED_BYTE, image );

    if (isPowerOf2(image.width) && isPowerOf2(image.height)) {
          // Yes, it's a power of 2. Generate mips.
          gl.generateMipmap(gl.TEXTURE_2D);
          gl.texParameteri( gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER,
            gl.NEAREST_MIPMAP_LINEAR );
          gl.texParameteri( gl.TEXTURE_2D, gl.TEXTURE_MAG_FILTER, gl.NEAREST );
    } else {
          // No, it's not a power of 2. Turn off mips and set
          // wrapping to clamp to edge
          gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_S, gl.CLAMP_TO_EDGE);
          gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_T, gl.CLAMP_TO_EDGE);
          gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.LINEAR);
       }

    gl.uniform1i(gl.getUniformLocation(program, "texture"), 0);
}

function isPowerOf2(value) {
  return (value & (value - 1)) == 0;
}


var texSizeA = 128;

var image1 = new Array()
    for (var i =0; i<texSizeA; i++)  image1[i] = new Array();
    for (var i =0; i<texSizeA; i++)
        for ( var j = 0; j < texSizeA; j++)
           image1[i][j] = new Float32Array(4);
    for (var i =0; i<texSizeA; i++) for (var j=0; j<texSizeA; j++) {
        var c = (((i & 0x8) == 0) ^ ((j & 0x8)  == 0));
        image1[i][j] = [c, c, c, 1];
    }

// Convert floats to ubytes for texture
var image2 = new Uint8Array(4*texSizeA*texSizeA);
    for ( var i = 0; i < texSizeA; i++ )
        for ( var j = 0; j < texSizeA; j++ )
           for(var k =0; k<4; k++)
                image2[4*texSizeA*i+4*j+k] = 255*image1[i][j][k];


function triangle(a, b, c) {

     normalsArray.push(a[0], a[1], a[2], 0.0);
     normalsArray.push(b[0], b[1], b[2], 0.0);
     normalsArray.push(c[0], c[1], c[2], 0.0);

     pointsArray.push(a);
     pointsArray.push(b);
     pointsArray.push(c);

     index += 3;

}


function divideTriangle(a, b, c, count) {
    if ( count > 0 ) {
        var ab = mix( a, b, 0.5);
        var ac = mix( a, c, 0.5);
        var bc = mix( b, c, 0.5);

        ab = normalize(ab, true);
        ac = normalize(ac, true);
        bc = normalize(bc, true);

        divideTriangle( a, ab, ac, count - 1 );
        divideTriangle( ab, b, bc, count - 1 );
        divideTriangle( bc, c, ac, count - 1 );
        divideTriangle( ab, bc, ac, count - 1 );
    }
    else {
        triangle( a, b, c );
    }
}


function tetrahedron(a, b, c, d, n) {
    divideTriangle(a, b, c, n);
    divideTriangle(d, c, b, n);
    divideTriangle(a, d, b, n);
    divideTriangle(a, c, d, n);
}

window.onload = function init() {

    canvas = document.getElementById( "gl-canvas" );

    gl = WebGLUtils.setupWebGL( canvas );
    if ( !gl ) { alert( "WebGL isn't available" ); }

    gl.viewport( 0, 0, canvas.width, canvas.height );
    gl.clearColor( 0.0, 0.0, 0.0, 1.0 );

    gl.enable(gl.DEPTH_TEST);

    //
    //  Load shaders and initialize attribute buffers
    //
    program = initShaders( gl, "vertex-shader", "fragment-shader" );
    gl.useProgram( program );

    ambientProduct  = mult(lightAmbient,  materialAmbient);
    diffuseProduct  = mult(lightDiffuse,  materialDiffuse);
    specularProduct = mult(lightSpecular, materialSpecular);

    tetrahedron(va, vb, vc, vd, numTimesToSubdivide);

    var vBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, vBuffer);
    gl.bufferData(gl.ARRAY_BUFFER, flatten(pointsArray), gl.STATIC_DRAW);

    var vPosition = gl.getAttribLocation( program, "vPosition");
    gl.vertexAttribPointer(vPosition, 4, gl.FLOAT, false, 0, 0);
    gl.enableVertexAttribArray(vPosition);

    modelViewMatrixLoc = gl.getUniformLocation( program, "modelViewMatrix" );
    projectionMatrixLoc = gl.getUniformLocation( program, "projectionMatrix" );

    document.getElementById("Button2").onclick = function(){theta += dr;};
    document.getElementById("Button3").onclick = function(){theta -= dr;};
    document.getElementById("Button4").onclick = function(){phi += dr;};
    document.getElementById("Button5").onclick = function(){phi -= dr;};

    document.getElementById("Button6").onclick = function(){
        numTimesToSubdivide++;
        index = 0;
        pointsArray = [];
        init();
    };

    document.getElementById("Button7").onclick = function(){
        if(numTimesToSubdivide) numTimesToSubdivide--;
        index = 0;
        pointsArray = [];
        init();
    };
	
	  gl.uniform1i( gl.getUniformLocation(program, "chosenMap"),(chosenMap) );
	
	
	  gl.uniform4fv( gl.getUniformLocation(program, "colorVec"),flatten(colorVec) );
	   
    configureTexture(document.getElementById("texImage"));

    render();
}


function render() {

    gl.clear( gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);

    eye = vec3(radius*Math.sin(theta)*Math.cos(phi),
        radius*Math.sin(theta)*Math.sin(phi), radius*Math.cos(theta));

    modelViewMatrix = lookAt(eye, at , up);
    projectionMatrix = ortho(left, right, bottom, ytop, near, far);


    gl.uniformMatrix4fv(modelViewMatrixLoc, false, flatten(modelViewMatrix) );
    gl.uniformMatrix4fv(projectionMatrixLoc, false, flatten(projectionMatrix) );


    for( var i=0; i<index; i+=3)
        gl.drawArrays( gl.TRIANGLES, i, 3 );

    window.requestAnimFrame(render);
}

var descriptions = ["1. To begin Ray-tracing, we need 3 things: the viewpoint, the resolution of the image (pixels), and the scene to be rendered. This example uses the viewpoint represented by the eye, a 12x11 resolution image, and a simple scene with a cube and a cylinder.", 
"2. Now that we have our setup completed, we can begin generating the viewing rays. The rays are drawn from the viewpoint, through the center of the pixel. The pixel that is used by the ray is marked.",
"3. Another viewing ray is generated.",
"4. Another viewing ray is generated.",
"5. Another viewing ray is generated.",
"6. With our rays generated, we can use them to calculate intersections with objects in our scene. The first ray extends through the pixel, but does not hit any objects, so its a 'miss'",
"7. The second ray intersects the orange cube, so we have a 'hit' where the intersection occurs.",
"8. The third ray intersects both the orange cube and the green cylinder. Which object should be recorded according to what you learned above?",
"9. The last ray intersects the green cylinder only.",
"10. All intersections have now been computed. Intersections that will be used for shading are marked with blue x's, and intersections or misses that are ignored are marked with red x's.",
"11. With all intersections computed, we can begin shading. The first ray did not intersect anything in the scene, so the pixel is set to the background color of the scene (white)",
"12. The second ray intersected the cube, so the pixel is set to the color of the cube (orange),",
"13. The third ray intersected both objects in the scene, but we determined that the cube intersected closer to the viewpoint, so the pixel is set to the color of the cube (orange)",
"14. The last ray intersected only the cylinder, so the pixel is set to the color of cylinder (green).",
"15. The final product of our example."];

window.onload = function init() {
  document.getElementById("description").innerHTML = descriptions[0];
  document.getElementById("8quizDiv").style.display = "none";
}

var index=0;
function nextPic()
{
  index++;
  if (index >= 15) {
    index = 0;
  }
  document.getElementById("tutorial").src = "Images/"+(index+1)+".png";
  document.getElementById("description").innerHTML = descriptions[index];
  if(index == 7) {
    document.getElementById("8quizDiv").style.display = "block";
  } else {
    document.getElementById("8quizDiv").style.display = "none";
  }
}

function lastPic()
{
  index--;
  if (index < 0) {
    index = 0;
  }
  document.getElementById("tutorial").src = "Images/"+(index+1)+".png";
  document.getElementById("description").innerHTML = descriptions[index];
  if(index == 7) {
    document.getElementById("8quizDiv").style.display = "block";
  } else {
    document.getElementById("8quizDiv").style.display = "none";
  }
}

function displayRight() 
{
  document.getElementById("8answer").innerHTML = "Correct! The orange cube intersects the ray closer to the viewpoint and should be chosen over the green cylinder.";
}

function displayWrong() 
{
  document.getElementById("8answer").innerHTML = "Incorrect! The orange cube intersects the ray closer to the viewpoint and should be chosen over the green cylinder.";
}

function checkQuiz() {
  var right = 0;
  if(document.getElementById("1right").checked) {
    right++;
    document.getElementById("answer1").style = "color: green";
    document.getElementById("answer1").innerHTML = "Correct!";
  } else {
    document.getElementById("answer1").style = "color: red";
    document.getElementById("answer1").innerHTML = "Incorrect.";
  }

  if(document.getElementById("2right").checked) {
    right++;
    document.getElementById("answer2").style = "color: green";
    document.getElementById("answer2").innerHTML = "Correct!";
  } else {
    document.getElementById("answer2").style = "color: red";
    document.getElementById("answer2").innerHTML = "Incorrect.";
  }

  if(document.getElementById("3right").checked) {
    right++;
    document.getElementById("answer3").style = "color: green";
    document.getElementById("answer3").innerHTML = "Correct!";
  } else {
    document.getElementById("answer3").style = "color: red";
    document.getElementById("answer3").innerHTML = "Incorrect.";
  }

  if(document.getElementById("4right").checked) {
    right++;
    document.getElementById("answer4").style = "color: green";
    document.getElementById("answer4").innerHTML = "Correct!";
  } else {
    document.getElementById("answer4").style = "color: red";
    document.getElementById("answer4").innerHTML = "Incorrect.";
  }

  if(document.getElementById("5right").checked) {
    right++;
    document.getElementById("answer5").style = "color: green";
    document.getElementById("answer5").innerHTML = "Correct!";
  } else {
    document.getElementById("answer5").style = "color: red";
    document.getElementById("answer5").innerHTML = "Incorrect.";
  }

  if(document.getElementById("6right").checked) {
    right++;
    document.getElementById("answer6").style = "color: green";
    document.getElementById("answer6").innerHTML = "Correct!";
  } else {
    document.getElementById("answer6").style = "color: red";
    document.getElementById("answer6").innerHTML = "Incorrect.";
  }

  if(document.getElementById("7right").checked) {
    right++;
    document.getElementById("answer7").style = "color: green";
    document.getElementById("answer7").innerHTML = "Correct!";
  } else {
    document.getElementById("answer7").style = "color: red";
    document.getElementById("answer7").innerHTML = "Incorrect.";
  }

  if(document.getElementById("8right").checked) {
    right++;
    document.getElementById("answer8").style = "color: green";
    document.getElementById("answer8").innerHTML = "Correct!";
  } else {
    document.getElementById("answer8").style = "color: red";
    document.getElementById("answer8").innerHTML = "Incorrect.";
  }

  document.getElementById("answers").innerHTML = "You got " + right + " questions right out of 8."

}

<?php
$servername = "mysql.cs.iastate.edu";
$username = "dbu309yt8";
$password = "TarDx@ew";
$dbname = "db309yt8";

$conn = mysqli_connect($servername,$username,$password,$dbname);
if(!$conn) {
	die("Connection failed: " . mysqli_connect_error());
}

if(isset($_POST['login_id']) && isset($_POST['recipe_name'])) {
	$login_id = $_POST['login_id'];
	$recipe_name = $_POST['recipe_name'];
	$query = "SELECT recipe_id FROM Recipe WHERE login_id = '$login_id' AND recipe_name = '$recipe_name';";
	$result = mysqli_query($conn, $query);
	if ($result->num_rows > 0) {
		$row = mysqli_fetch_row($result);
		$id = $row[0];
		$secondQuery = "SELECT a.ingredient_id FROM RecipeIngredient a WHERE recipe_id = '$id';";
		$secondResult = mysqli_query($conn, $secondQuery);
		if ($secondResult->num_rows > 0) {
			while($rowX =  mysqli_fetch_row($secondResult)) {
				$ingredientId = $rowX[0];
				$thirdQuery = "SELECT a.ingredient_name FROM Ingredient a WHERE ingredient_id = '$ingredientId';";
				$thirdResult = mysqli_query($conn, $thirdQuery);
				if ($thirdResult->num_rows > 0) {
					while($rowY =  mysqli_fetch_row($thirdResult)) {
						echo $rowY[0] . ",";
					}
				}
			}
		}
		else {
			echo "Can't find ingredients.";
		}
	}
	else {
		echo "Can't find recipe.";
	}
	//Ingredients WHERE recipe_id = (SELECT recipe_id FROM userRecipe WHERE recipe_name = 'Pizza' AND login_id = 'ravi');
}

$conn->close();
exit;
?>

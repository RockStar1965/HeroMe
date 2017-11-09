<?php

require_once '../include/DbHandler.php';
require_once '../include/PassHash.php';
require '.././libs/Slim/Slim.php';

\Slim\Slim::registerAutoloader();

$app = new \Slim\Slim();

// User id from db - Global Variable
$user_id = NULL;

/**
 * Adding Middle Layer to authenticate every request
 * Checking if the request has valid api key in the 'Authorization' header
 */
function authenticate(\Slim\Route $route) {
    // Getting request headers
    $headers = apache_request_headers();
    $response = array();
    $app = \Slim\Slim::getInstance();

    // Verifying Authorization Header
    if (isset($headers['Authorization'])) {
        $db = new DbHandler();

        // get the api key
        $apikey = $headers['Authorization'];
        // validating apikey
        if (!$db->isValidApiKey($apikey)) {
            // apikey is not present in users table
            $response['error'] = "true";
            $response['rest'] = "authenticate";
		    $response['message'] = "Apikey Error";
            echoRespnse(401, $response);
            $app->stop();
        } else {
            global $user_id;
            // get user primary key id
            $user_id = $db->getUserId($apikey);
        }
    } else {
        // apikey is missing in header
		$response['error'] = "true";
		$response['rest'] = "authenticate";
		$response['message'] = "Apikey Missing";
        echoRespnse(400, $response);
        $app->stop();
    }
}

/**
 * ----------- METHODS WITHOUT AUTHENTICATION ---------------------------------
 */
/**
 * User Registration
 * url - /register
 * method - POST
 * params - name, email, password
 */
$app->post('/register', function() use ($app) {
            // check for required params
            verifyRequiredParams(array('name', 'username', 'email', 'password','source'));

            $response = array();

            // reading post params
            $name = $app->request->post('name');
            $username = $app->request->post('username');
            $email = $app->request->post('email');
            $password = $app->request->post('password');
            $source = $app->request->post('source');

            // validating email address
            validateEmail($email);

            $db = new DbHandler();
            $res = $db->createUser($name, $username, $email, $password);

            if ($res == USER_CREATED_SUCCESSFULLY) {
            	$user = $db->getUserByEmail($email);
            	if ($user != NULL) {
					$response['error'] = "false";
					$response['rest'] = "login";
					$response['source'] = $source;
					$response['apikey'] = $user['apikey'];
				} else {
					// unknown error occurred
					$response['error'] = "true";
					$response['rest'] = "login";
					$response['source'] = $source;
					$response['message'] = "Unknown error";
				}
            } else if ($res == USER_CREATE_ERROR) {
                $response['error'] = "true";
                $response['rest'] = "register";
                $response['source'] = $source;
                $response['message'] = "Failed to create Error";
            } else if ($res == USER_CREATE_FAILED) {
                $response['error'] = "true";
                $response['rest'] = "register";
                $response['source'] = $source;
                $response['message'] = "Failed to create User";
            } else if ($res == USER_ALREADY_EXISTED) {
                $response['error'] = "true";
                $response['rest'] = "register";
                $response['source'] = $source;
                $response['message'] = "Email Already Exist";
            } else if ($res == USERNAME_ALREADY_EXISTED) {
                $response['error'] = "true";
                $response['rest'] = "register";
                $response['source'] = $source;
                $response['message'] = "Username Already Exist";
            }
            // echo json response
            echoRespnse(201, $response);
        });

/**
 * User Login
 * url - /login
 * method - POST
 * params - email, password
 */
$app->post('/login', function() use ($app) {
            // check for required params
            verifyRequiredParams(array('email', 'password', 'source', 'game'));

            // reading post params
            $email = $app->request()->post('email');
            $password = $app->request()->post('password');
            $game = $app->request->post('game');
            $source = $app->request->post('source');
            $response = array();

            $db = new DbHandler();
            // check for correct email and password
            if ($db->checkLogin($email, $password)) {
                // get the user by email
                $user = $db->getUserByEmail($email);

            	if ($user != NULL) {
					$response['error'] = "false";
					$response['rest'] = "login";
                    $response['source'] = $source;
					$response['apikey'] = $user['apikey'];
					$response['maxlevel'] = $user['maxlevel'];
					$response['curlevel'] = $user['curlevel'];
					$response['name'] = $user['name'];
					$response['username'] = $user['username'];
				} else {
					// unknown error occurred
					$response['error'] = "true";
					$response['rest'] = "login";
                    $response['source'] = $source;
					$response['message'] = "Unknown error";
				}

            } else {
                // user credentials are wrong
                $response['error'] = "true";
                $response['rest'] = "login";
                $response['source'] = $source;
                $response['message'] = "Error with credentials";
            }

            echoRespnse(200, $response);
        });

/*
 * ------------------------ METHODS WITH AUTHENTICATION ------------------------
 */

/**
 * Listing all tasks of particual user
 * method GET
 * url /tasks
 * TODO Not working for some reason. 2017-10-17
 */
$app->get('/tasks', 'authenticate', function() {
            global $user_id;
            $response = array();
            $db = new DbHandler();

            // fetching all user tasks
            $result = $db->getAllUserTasks($user_id);

            $response['error'] = "false";
            $response['tasks'] = array();

            // looping through result and preparing tasks array
            while ($task = $result->fetch_assoc()) {
                $tmp = array();
                $tmp['id'] = $task['id'];
                $tmp['task'] = $task['task'];
                $tmp['status'] = $task['status'];
                $tmp['createdAt'] = $task['created_at'];
                array_push($response['tasks'], $tmp);
            }
            echoRespnse(200, $response);
        });

/**
 * Listing single task of particual user
 * method GET
 * url /tasks/:id
 * Will return 404 if the task doesn't belongs to user
 */
$app->get('/tasks/:id', 'authenticate', function($task_id) {
            global $user_id;
            $response = array();
            $db = new DbHandler();

            // fetch task
            $result = $db->getTask($task_id, $user_id);

            if ($result != NULL) {
                $response['error'] = "false";
                $response['rest'] = "post-tasks:id";
                $response['id'] = $result['id'];
                $response['task'] = $result['task'];
                $response['status'] = $result['status'];
                $response['created_at'] = $result['created_at'];
                echoRespnse(200, $response);
            } else {
                $response['error'] = "true";
                $response['rest'] = "post-tasks:id";
 				echoRespnse(404, $response);
            }
        });

/**
 * Creating new task in db
 * method POST
 * params - name
 * url - /tasks/
 */
$app->post('/tasks', 'authenticate', function() use ($app) {
            // check for required params
            verifyRequiredParams(array('task','source'));

            $response = array();
            $task = $app->request->post('task');
            $source = $app->request->post('source');

            global $user_id;
            $db = new DbHandler();

            // creating new task
            $task_id = $db->createTask($user_id, $task);

            if ($task_id != NULL) {
                $response['error'] = "false";
                $response['rest'] = "post-tasks";
                $response['source'] = $source;
                $response['task_id'] = $task_id;
                echoRespnse(201, $response);
            } else {
                $response['error'] = "true";
                $response['rest'] = "post-tasks";
                $response['source'] = $source;
                echoRespnse(200, $response);
            }
        });





/**
 * Creating new score in db
 * method POST
 * params - name
 * url - /score/
 */
$app->post('/score', 'authenticate', function() use ($app) {
            // check for required params
            verifyRequiredParams(array('score','score1','score2','game','level','source'));

            $response = array();
            $score = $app->request->post('score');
            $score1 = $app->request->post('score1');
            $score2 = $app->request->post('score2');
            $game = $app->request->post('game');
      		$level = $app->request->post('level');
            $source = $app->request->post('source');

            global $user_id;
            $db = new DbHandler();

            // creating new task
            $param = $db->createScore($user_id, $score, $score1, $score2, $game, $level);

			for ( $i = 1; $i <= 10; $i++) {
				$param1 .= $db->getHighScore($game, $i, 0) . "#";
			}
			for ( $i = 1; $i <= 10; $i++) {
				$param2 .= $db->getHighScore($game, $i, $user_id) . "#";
			}

            if ($param != NULL) {
                $response['error'] = "false";
                $response['rest'] = "post-score";
                $response['source'] = $source;
                $response['rank'] = $param['rank'];
                $response['highscore_all'] = $param1;
                $response['highscore_user'] = $param2;
                $response['maxlevel'] = $param['maxlevel'];
				$response['curlevel'] = $param['curlevel'];
                $response['status'] = $param['status'];
                echoRespnse(201, $response);
            } else {
                $response['error'] = "true";
                $response['rest'] = "post-score";
                $response['source'] = $source;
                echoRespnse(200, $response);
            }
        });



/**
 * Creating new score in db
 * method POST
 * params - name
 * url - /score/
 */
$app->post('/highscore', 'authenticate', function() use ($app) {
            // check for required params
            verifyRequiredParams(array('game','level','source'));

            $response = array();
            $game = $app->request->post('game');
            $level = $app->request->post('level');
            $source = $app->request->post('source');

            global $user_id;
            $db = new DbHandler();

			for ( $i = 1; $i <= 10; $i++) {
				$param1 .= $db->getHighScore($game, $i, 0) . "#";
			}
			for ( $i = 1; $i <= 10; $i++) {
				$param2 .= $db->getHighScore($game, $i, $user_id) . "#";
			}
            if (($param1 != NULL) && ($param2 != NULL)){
                $response['error'] = "false";
                $response['rest'] = "post-highscore";
                $response['source'] = $source;
                $response['highscore_all'] = $param1;
                $response['highscore_user'] = $param2;
                echoRespnse(201, $response);
            } else {
                $response['error'] = "true";
                $response['rest'] = "post-highscore";
                $response['source'] = $source;
                echoRespnse(200, $response);
            }
        });


/**
 * Updating existing task
 * method PUT
 * params task, status
 * url - /tasks/:id
 */
$app->put('/tasks/:id', 'authenticate', function($task_id) use($app) {
            // check for required params
            verifyRequiredParams(array('task', 'status', 'source'));

            global $user_id;
            $task = $app->request->put('task');
            $status = $app->request->put('status');
            $source = $app->request->put('source');

            $db = new DbHandler();
            $response = array();

            // updating task
            $result = $db->updateTask($user_id, $task_id, $task, $status);
            if ($result) {
                // task updated successfully
                $response['error'] = "false";
                $response['rest'] = "put-tasks:id";
                $response['source'] = $source;
          } else {
                // task failed to update
                $response['error'] = "true";
                $response['rest'] = "put-tasks:id";
                $response['source'] = $source;
          }
            echoRespnse(200, $response);
        });

/**
 * Deleting task. Users can delete only their tasks
 * method DELETE
 * url /tasks
 */
$app->delete('/tasks/:id', 'authenticate', function($task_id) use($app) {
            global $user_id;

            $db = new DbHandler();
            $response = array();
            $result = $db->deleteTask($user_id, $task_id);
            if ($result) {
                // task deleted successfully
                $response['error'] = "false";
                $response['rest'] = "delete-tasks:id";
                $response['source'] = $source;
          } else {
                // task failed to delete
                $response['error'] = "true";
                $response['rest'] = "delete-tasks:id";
	            $response['source'] = $source;
	      }
            echoRespnse(200, $response);
        });

/**
 * Verifying required params posted or not
 */
function verifyRequiredParams($required_fields) {
    $error = false;
    $error_fields = '';
    $request_params = array();
    $request_params = $_REQUEST;
    // Handling PUT request params
    if ($_SERVER['REQUEST_METHOD'] == 'PUT') {
        $app = \Slim\Slim::getInstance();
        parse_str($app->request()->getBody(), $request_params);
    }
    foreach ($required_fields as $field) {
        if (!isset($request_params[$field]) || strlen(trim($request_params[$field])) <= 0) {
            $error = true;
            $error_fields .= $field . ', ';
        }
    }

    if ($error) {
        // Required field(s) are missing or empty
        // echo error json and stop the app
        $response = array();
        $app = \Slim\Slim::getInstance();
        $response['error'] = "true";
        $response['rest'] = "verifyRequiredParams";
        $response['message'] = "Required field(s) " . substr($error_fields, 0, -2) . " is missing or empty";
        echoRespnse(400, $response);
        $app->stop();
    }
}

/**
 * Validating email address
 */
function validateEmail($email) {
    $app = \Slim\Slim::getInstance();
    if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
        $response['error'] = "true";
        $response['rest'] = "validateEmail";
        echoRespnse(400, $response);
        $app->stop();
    }
}

/**
 * Echoing json response to client
 * @param String $status_code Http response code
 * @param Int $response Json response
 */
function echoRespnse($status_code, $response) {
    $app = \Slim\Slim::getInstance();
    // Http response code
    $app->status($status_code);

    // setting response content type to json
    $app->contentType('application/json');

    echo json_encode($response);
}

$app->run();
?>

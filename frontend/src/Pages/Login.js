import React, {useState} from 'react';
// import { useHistory } from 'react-router-dom';

import {
    MDBBtn,
    MDBCard,
    MDBCardBody,
    MDBCol,
    MDBContainer,
    MDBIcon,
    MDBInput,
    MDBRow
} from 'mdb-react-ui-kit';

function Login() {

    const [username, setName] = useState('');
    const [password, setPassword] = useState('');

    const handleLogin = async () => {
        // Prepare the data to send to the server
        const pattern = /([a-zA-Z0-9]+(?:[._+-][a-zA-Z0-9]+)*)@([a-zA-Z0-9]+(?:[.-][a-zA-Z0-9]+)*[.][a-zA-Z]{2,})/;

        if ((username.length === 0 && password.length === 0 )|| username.length === 0 || password.length === 0) {
            alert("All fields are required.");
            return;
        } else if (username.length < 4) {
            alert(`Invalid username`);
            return;

        }
        console.log(`Input data matches the patterns, processing the request...`);

        const userData = {
            username,
            password,
        };

        const response = await fetch('http://localhost:8080/login', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(userData)
        });

        // Parse the JSON response


        if (response.ok) {
            //funny alert
            alert(`Welcome to the best app ever. If you are happy, click "ok" :)!`);

            // If the login is successful, extract the JWT token from the response
            const data = await response.json();
            const token = data?.data?.token;

            // Store the token in local storage
            localStorage.setItem('token', "Bearer "+token);

            // Redirection to /login/success
            window.location.href = '/login/success';

        } else {
            const errorMessage = await response.text();
            const result = errorMessage.substring(10, errorMessage.length - 2);
            // happens only if there is something that has not been caught by frontend validation
            console.error("Server's response: " + result);
            alert(result);
        }
    };

    return (
        <MDBContainer fluid>
            <MDBCard className='text-black m-5' style={{borderRadius: '25px'}}>
                <MDBCardBody>
                    <MDBRow>
                        <MDBCol md='10' lg='6' className='order-2 order-lg-1 d-flex flex-column align-items-center'>
                            <p className="text-center h1 fw-bold mb-5 mx-1 mx-md-4 mt-4">Login</p>
                            <div className="d-flex flex-row align-items-center mb-4">
                                <MDBIcon fas icon="user me-3" size='lg'/>
                                <MDBInput label='Username' id='form1' type='text' className='w-100'
                                          onChange={(e) => setName(e.target.value)}/>
                            </div>
                            <div className="d-flex flex-row align-items-center mb-4">
                                <MDBIcon fas icon="lock me-3" size='lg'/>
                                <MDBInput label='Password' id='form3' type='password'
                                          onChange={(e) => setPassword(e.target.value)}/>
                            </div>
                            <MDBBtn className='mb-4' size='lg' onClick={handleLogin}>Login</MDBBtn>
                        </MDBCol>
                    </MDBRow>
                </MDBCardBody>
            </MDBCard>
        </MDBContainer>
    );
}

export default Login;

import React, {useState} from 'react';
// import { useHistory } from 'react-router-dom';

import {
    MDBBtn,
    MDBCard,
    MDBCardBody,
    MDBCardImage,
    MDBCheckbox,
    MDBCol,
    MDBContainer,
    MDBIcon,
    MDBInput,
    MDBRow
} from 'mdb-react-ui-kit';

function Register() {
    // const history = useHistory();

    const [username, setName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');

    const handleRegister = async () => {
        // Prepare the data to send to the server
        const pattern = /([a-zA-Z0-9]+(?:[._+-][a-zA-Z0-9]+)*)@([a-zA-Z0-9]+(?:[.-][a-zA-Z0-9]+)*[.][a-zA-Z]{2,})/;

        if (username.length === 0 && email.length === 0 && password.length === 0) {
            alert("Name, email and password are required.");
            return;
        } else if (username.length < 4) {
            alert(`Name must be at least 4 character long`);
            return;
        } else if (!pattern.test(email)) {
            alert(`Invalid e-mail address!`);
            return;
        } else if (password.length < 8) {
            alert(`Password must be at least 8 character.`)
            return;
        } else if (password !== confirmPassword) {
            alert(`Passwords do NOT matches!`);
            return;
        }
        console.log(`Input data matches the patterns, processing the request...`);

        const userData = {
            username,
            email,
            password,
        };

        const response = await fetch('http://localhost:8080/register', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(userData)
        });

        if (response.ok) {
            // const data = await response.json();
            alert(`Done! Confirm registration by opening the link sent to ${email}`);
            // history.push('/login');
            // history('/login');
            window.location.href = '/login';
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
                            <p className="text-center h1 fw-bold mb-5 mx-1 mx-md-4 mt-4">Sign up</p>
                            <div className="d-flex flex-row align-items-center mb-4">
                                <MDBIcon fas icon="user me-3" size='lg'/>
                                <MDBInput label='Your Name' id='form1' type='text' className='w-100'
                                          onChange={(e) => setName(e.target.value)}/>
                            </div>
                            <div className="d-flex flex-row align-items-center mb-4">
                                <MDBIcon fas icon="envelope me-3" size='lg'/>
                                <MDBInput label='Your Email' id='form2' type='email'
                                          onChange={(e) => setEmail(e.target.value)}/>
                            </div>
                            <div className="d-flex flex-row align-items-center mb-4">
                                <MDBIcon fas icon="lock me-3" size='lg'/>
                                <MDBInput label='Password' id='form3' type='password'
                                          onChange={(e) => setPassword(e.target.value)}/>
                            </div>
                            <div className="d-flex flex-row align-items-center mb-4">
                                <MDBIcon fas icon="key me-3" size='lg'/>
                                <MDBInput label='Repeat your password' id='form4' type='password'
                                          onChange={(e) => setConfirmPassword(e.target.value)}/>
                            </div>
                            <div className='mb-4'>
                                <MDBCheckbox name='flexCheck' value='' id='flexCheckDefault'
                                             label='Subscribe to our newsletter'/>
                            </div>
                            <MDBBtn className='mb-4' size='lg' onClick={handleRegister}>Register</MDBBtn>
                        </MDBCol>

                        <MDBCol md='10' lg='6' className='order-1 order-lg-2 d-flex align-items-center'>
                            <MDBCardImage
                                src='https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-registration/draw1.webp'
                                fluid
                            />
                        </MDBCol>
                    </MDBRow>
                </MDBCardBody>
            </MDBCard>
        </MDBContainer>
    );
}

export default Register;

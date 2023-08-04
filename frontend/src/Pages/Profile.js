import React, { useState, useEffect } from "react";
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

const Profile = () => {
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [initialName, setInitialName] = useState(""); // To track if the name was changed

    useEffect(() => {
        const token = localStorage.getItem("token");
        if (token) {
            const stringArray = token.split(".");
            const user = JSON.parse(atob(stringArray[1]));
            setInitialName(user.name || ""); // Set the initial name
            console.log(user.sub);
        }
    }, []);

    const handleFormSubmit = async (e) => {
        e.preventDefault();
        if (!name && !email && !password) {
            alert("A field is required to be filled.");
            return;
        }
        if (name.length < 4 && name.length > 0) {
            alert("Username must be at least 4 characters long");
            return;
        }

        if (password.length > 0 && password.length < 8) {
            alert("Password must be at least 8 characters long");
            return;
        }
        const updatedUser = {
            name,
            email,
            password,
        };

        const response = await fetch("http://localhost:8080/users", {
            method: "PATCH",
            headers: {
                "Content-Type": "application/json",
                Authorization: localStorage.getItem("token"),
            },
            body: JSON.stringify(updatedUser),
        });
        if (response.ok) {
            setEmail("");
            setPassword("");
            alert("Profile updated successfully!");
            if (name !== initialName) {
                localStorage.removeItem("token");
                window.location.replace("/login");
            }
        } else {
            const errorMessage = await response.text();
            const result = errorMessage.substring(10, errorMessage.length - 2);
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
                            <p className="text-center h1 fw-bold mb-5 mx-1 mx-md-4 mt-4">You can update your profile here :)</p>
                            <div className="d-flex flex-row align-items-center mb-4">
                                <MDBIcon fas icon="user me-3" size='lg'/>
                                <MDBInput label='Your Name' value={name} id='form1' type='text' className='w-100'
                                          onChange={(e) => setName(e.target.value)}/>
                            </div>
                            <div className="d-flex flex-row align-items-center mb-4">
                                <MDBIcon fas icon="envelope me-3" size='lg'/>
                                <MDBInput label='Your Email' value={email} id='form2' type='email'
                                          onChange={(e) => setEmail(e.target.value)}/>
                            </div>
                            <div className="d-flex flex-row align-items-center mb-4">
                                <MDBIcon fas icon="lock me-3" size='lg'/>
                                <MDBInput label='Password' value={password} id='form3' type='password'
                                          onChange={(e) => setPassword(e.target.value)}/>
                            </div>
                            <MDBBtn className='mb-4' size='lg' onClick={handleFormSubmit}>Update Profile</MDBBtn>
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
        </MDBContainer>)
};

export default Profile;

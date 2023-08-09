import React, { useEffect, useState } from "react";
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

const Instances = () => {
    const [os, setOs] = useState('');
    const [count, setCount] = useState('');
    const [size, setSize] = useState('');
    const [region, setRegion] = useState('');

    useEffect(() => {
        let token = localStorage.getItem("token");
    }, []);

    const createInstance = async () => {
        if (!os || !count || !size || !region) {
            alert('Please provide all instance specifications.');
            return;
        }
        if (count <= 0) {
            alert('Count must be a positive number.');
            return;
        }

        const instanceData = {
            os,
            count,
            size,
            region,
        };
            const response = await fetch("http://localhost:8080/ec2/instances", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: localStorage.getItem("token"),
                },
                body: JSON.stringify(instanceData),
            });

            if (response.ok) {
                alert(`Confirmation email was sent to you. Please, check if everything went smoothly.`);

            } else {
                const errorMessage = await response.text();
                const result = errorMessage.substring(10, errorMessage.length - 2);
                // happens only if there is something that has not been caught by frontend validation
                console.error("Server's response: " + result);
                alert(result);
            }

    };
    const handleInstance = (e) => {
        e.preventDefault();
        createInstance();
    };

    return (
        <MDBContainer fluid>
            <MDBCard className='text-black m-5' style={{borderRadius: '25px'}}>
                <MDBCardBody>
                    <MDBRow>
                        <MDBCol md='10' lg='6' className='order-2 order-lg-1 d-flex flex-column align-items-center'>
                            <p className="text-center h1 fw-bold mb-5 mx-1 mx-md-4 mt-4">Create your instances!</p>

                            <div style={{ marginBottom: '1rem' }}>
                                <label style={{ fontSize: '1.2rem' }}>Amazon Machine operating system:</label>
                                <select value={os} onChange={(e) => setOs(e.target.value)} style={{ fontSize: '1rem', marginLeft: '1rem' }}>
                                    <option value="">Select OS</option>
                                    <option value="Amazon Linux">Amazon Linux</option>
                                    <option value="Ubuntu">Ubuntu</option>
                                </select>
                            </div>
                            <div style={{ marginBottom: '1rem' }}>
                                <label style={{ fontSize: '1.2rem' }}>How many instances should be created:</label>
                                <input
                                    type="number"
                                    value={count}
                                    onChange={(e) => setCount(e.target.value)}
                                    style={{
                                        fontSize: '1rem',
                                        padding: '0.5rem',
                                        border: '1px solid #ccc',
                                        borderRadius: '2px',
                                        width: '100px',
                                        height: '25px',
                                        marginTop: '0.5rem',
                                    }}
                                />
                            </div>
                            <div style={{ marginBottom: '1rem' }}>
                                <label style={{ fontSize: '1.2rem' }}>Region:</label>
                                <select value={region} onChange={(e) => setRegion(e.target.value)} style={{ fontSize: '1rem', marginLeft: '1rem' }}>
                                    <option value="">Select region</option>
                                    <option value="eu-west-3">eu-west-3</option>
                                </select>
                            </div>
                            <div style={{ marginBottom: '1rem' }}>
                                <label style={{ fontSize: '1.2rem' }}>Instance type:</label>
                                <select value={size} onChange={(e) => setSize(e.target.value)} style={{ fontSize: '1rem', marginLeft: '1rem' }}>
                                    <option value="">Select type</option>
                                    <option value="t3.micro">t3.micro</option>
                                    <option value="t2.micro">t2.micro</option>
                                </select>
                            </div>
                            <MDBBtn className='mb-4' size='lg' onClick={handleInstance}>Create instance</MDBBtn>
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
};

export default Instances;
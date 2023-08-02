import React from "react";
import { Link, NavLink, useNavigate } from "react-router-dom";

function Header() {
    let token = localStorage.getItem("token");

    const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.removeItem("token");   // Remove token from localStorage
        navigate('/');                      // Redirect to the home page
    };

    return (
        <>
            {token ? (
                <header>
                    <nav className="nav">
                        <Link to="/" className="site-title">Home</Link>
                        <NavLink to="/profile">Profile</NavLink>
                        <NavLink to="/instances">Instances</NavLink>
                        <Link to="/" onClick={handleLogout}>Logout</Link>
                    </nav>
              </header>
            ) : (
                <header>
                    <nav className="nav">
                        <Link to="/" className="site-title">Home</Link>
                        <NavLink to="/login">Login</NavLink>
                        <NavLink to="/register">Register</NavLink>
                    </nav>
                </header>
            )}        
        </>
    );
}

export default Header;
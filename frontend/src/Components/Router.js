import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Home from "../Pages/Home"
import Login from "../Pages/Login"
import Register from "../Pages/Register"
import Profile from "../Pages/Profile";
import Instances from "../Pages/Instances";
import Logout from "../Pages/Logout";
import Error from "../Pages/Error";
import Header from "./Header";


function Router() {
	return (
		<BrowserRouter>
            <Header />
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/login" element={<Login />} />
                <Route path="/login/success" element={<Home />} />
                <Route path="/register" element={<Register />} />
                <Route path="/profile" element={<Profile />} />
                <Route path="/instances" element={<Instances />} />
                <Route path="/logout" element={<Logout />} />
                <Route path="/*" element={<Error />} />
            </Routes>
		</BrowserRouter>
	)
}

export default Router;
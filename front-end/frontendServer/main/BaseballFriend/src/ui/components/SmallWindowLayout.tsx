import { Route, Routes } from 'react-router-dom';
import LoginPage from '../pages/window/login/LoginPage';
import TitleBar from './TitleBar';
import SignupPage from '../pages/window/login/SignupPage';

const SmallWindowLayout = () => {
  return (
    <div className="small-window-layout">
      <TitleBar />
      <Routes>
        <Route
          path="login"
          element={<LoginPage />}
        />
        <Route
          path="signup"
          element={<SignupPage />}
        />
      </Routes>
    </div>
  );
};

export default SmallWindowLayout;

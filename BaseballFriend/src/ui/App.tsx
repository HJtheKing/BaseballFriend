import LandingPage from './pages/window/landing/LandingPage';
import { HashRouter, Route, Routes } from 'react-router-dom';
import Cat from './pages/character/Cat';
import MainWindow from './components/WindowLayout';
import RealTime from './pages/window/realTime/RealtimePage';
import CharacterPage from './pages/character/CharacterPage';
import SmallWindowLayout from './components/SmallWindowLayout';

function App() {
  // window.eval = global.eval = function () {
  //   throw new Error(
  //     'Sorry, This app does not support window.eval() for security reasons.',
  //   );
  // }; // global이 undefined 남

  return (
    <HashRouter>
      <Routes>
        <Route
          path="/"
          element={<LandingPage />}
        />
        <Route
          path="/window/*"
          element={<MainWindow />}
        />
        <Route
          path="/smallWindow/*"
          element={<SmallWindowLayout />}
        />
        <Route
          path="/cat"
          element={<Cat />}
        />
        <Route
          path="/realtime"
          element={<RealTime />}
        />
        <Route
          path="/character"
          element={<CharacterPage />}
        />
      </Routes>
    </HashRouter>
  );
}

export default App;

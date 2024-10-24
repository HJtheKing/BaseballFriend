import GLBViewer from "./GLBViewer";
import "./App.css";
function App() {
  return (
    <div
      className="character"
      style={{
        width: "100vw",
        height: "100vh",
        background: "transparent",
      }}
    >
      <GLBViewer url="src/ui/assets/cat.glb" />
    </div>
  );
}

export default App;

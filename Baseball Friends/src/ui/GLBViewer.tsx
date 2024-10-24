import { useRef, useEffect } from "react";
import { Canvas, useFrame, useLoader, useThree } from "@react-three/fiber";
import { GLTFLoader } from "three-stdlib";
import { OrbitControls } from "@react-three/drei";
import * as THREE from "three";

interface ModelProps {
  url: string;
}

interface GLBViewerProps {
  url: string;
}

function Model({ url }: ModelProps) {
  const gltf = useLoader(GLTFLoader, url);
  const modelRef = useRef<THREE.Group>(null);
  const { scene, animations } = gltf;
  const { gl, scene: threeScene, camera } = useThree();

  useEffect(() => {
    // Set a fixed color for all meshes in the model
    scene.traverse((child) => {
      if (child instanceof THREE.Mesh) {
        child.material.color.set("#ffffff"); // Set your desired fixed color
      }
    });
  }, [scene]);

  useEffect(() => {
    if (animations.length) {
      const mixer = new THREE.AnimationMixer(scene);
      const action = mixer.clipAction(animations[0]);
      action.play();

      const animate = () => {
        mixer.update(0.016); // assumes 60fps
        gl.render(threeScene, camera);
        requestAnimationFrame(animate);
      };
      animate();

      return () => {
        mixer.stopAllAction();
      };
    }
  }, [animations, scene, gl, threeScene, camera]);

  // useFrame(() => {
  //   if (modelRef.current) {
  //     modelRef.current.rotation.y += 0.01; // Slowly rotate the model
  //   }
  // });

  return (
    <group ref={modelRef} position={[0, -1.0, 0]}>
      <primitive object={scene} scale={[1.2, 1.2, 1.2]} />
    </group>
  );
}

function GLBViewer({ url }: GLBViewerProps) {
  return (
    <div
      style={{
        width: "100%",
        height: "100%",
        position: "relative",
        background: "transparent",
      }}
    >
      <Canvas
        style={{ background: "transparent" }}
        camera={{ position: [0, 0.8, 1.8] }}
        gl={{ alpha: true }}
      >
        <ambientLight intensity={1} />
        <spotLight position={[10, 10, 10]} angle={0.15} penumbra={1} />
        <pointLight position={[-10, -10, -10]} />
        <Model url={url} />
        <OrbitControls enablePan={false} enableZoom={false} />
      </Canvas>
    </div>
  );
}

export default GLBViewer;

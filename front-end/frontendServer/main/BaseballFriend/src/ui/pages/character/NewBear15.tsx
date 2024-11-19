import { ThreeEvent, useLoader, useThree } from '@react-three/fiber';
import { GLTFLoader } from 'three-stdlib';
import * as THREE from 'three';
import { useCallback, useEffect, useRef, useState } from 'react';
import useCharacterStore from '../../store/useCharacterStore';
import { menuSize } from './CustomDropdown';
import { CameraControls } from '@react-three/drei';
import AnimationController from './AnimationController';

import { talkingSize } from './TalkingComponet';
import { alarmSize } from '../../components/Alarm';

const NewBear15 = () => {
  const [lastClickTime, setLastClickTime] = useState<number>(0);
  const DOUBLE_CLICK_DELAY = 800; // 더블클릭 간격 (밀리초)
  const cameraControlsRef = useRef<CameraControls>(null);
  const {
    menubar,
    fullSize,
    isDragging,
    wear,
    talking,
    newAlarm,
    // alwaysDisplay,
    alarmAnimationId,
    setMenumbar,
    setMenubarPos,
    setIgnoreMouseEvent,
    setIsDragging,
    setNowAnimation,
    setTalking,
    setTalkingPos,
    setNewAlarm,
    setAlwaysDisplay,
    setAlarmPos,
    setAlarmAnimationId,
  } = useCharacterStore();

  const { camera, raycaster, size } = useThree();
  const { scene, animations, nodes } = useLoader(
    GLTFLoader,
    `${wear.characterSerialNumber}.glb`,
  );
  let currentZ;
  const catRef = useRef<THREE.Group>();
  type ControllerRef = React.MutableRefObject<AnimationController>;
  const controllerRef = useRef<AnimationController>();
  const plane = new THREE.Plane(new THREE.Vector3(0, 0, 1), 0);
  const intersectionPoint = new THREE.Vector3();
  const mousePosition = new THREE.Vector2();
  const initialRotation = useRef<THREE.Euler>();
  let targetX: number;

  // 최초 카메라 설정
  useEffect(() => {
    const controls = cameraControlsRef.current;
    if (controls) {
      controls.setLookAt(0, 6, 3, 0, 5.2, 0, true).then(() => {
        controls.enabled = false;
        controls.minDistance = controls.distance;
        controls.maxDistance = controls.distance;
        controls.minPolarAngle = controls.polarAngle;
        controls.maxPolarAngle = controls.polarAngle;
        controls.minAzimuthAngle = controls.azimuthAngle;
        controls.maxAzimuthAngle = controls.azimuthAngle;
        controls.rotate(0, 0, true);
        controls.rotateTo(0, controls.polarAngle, true);
      });
    }
  }, []);

  // 마우스 클릭하면서 움직일 때
  const handleWindowMouseMove = (e: MouseEvent) => {
    if (!catRef.current) return;
    controllerRef.current?.stopAnimation();
    mousePosition.x = (e.clientX / size.width) * 2 - 1;
    mousePosition.y = -(e.clientY / size.height) * 2 + 1;

    raycaster.setFromCamera(mousePosition, camera);
    raycaster.ray.intersectPlane(plane, intersectionPoint);

    currentZ = catRef.current.position.z;
    // 0보다 작아지지 못하게 막기
    const newY = Math.max(intersectionPoint.y - 1, 0);
    catRef.current.position.set(
      intersectionPoint.x,
      newY, // 캐릭터 원점이 바닥이 1빼서 올려줌
      currentZ,
    );

    if (initialRotation.current) {
      catRef.current.rotation.copy(initialRotation.current);
    }
  };

  const handleClick = useCallback(
    async (e: ThreeEvent<PointerEvent>) => {
      e.stopPropagation();

      const currentTime = Date.now();
      setLastClickTime(currentTime);

      const timeDiff = currentTime - lastClickTime;

      if (timeDiff < DOUBLE_CLICK_DELAY) {
        if (talking) {
          setTalking(false);
          return;
        }

        const vector = new THREE.Vector3();
        vector.setFromMatrixPosition(e.object.matrixWorld);
        vector.project(e.camera);

        let x =
          (vector.x * 0.5 + 0.5) * window.innerWidth - talkingSize.width / 2;
        const y =
          (-vector.y * 0.5 + 0.5) * window.innerHeight -
          talkingSize.height -
          200;

        if (window.innerWidth - talkingSize.width < x) {
          x = window.innerWidth - talkingSize.width;
        } else if (x < 0) {
          x = 0;
        }
        setTalkingPos({ x, y });
        setTalking(true);
      }
    },
    [lastClickTime, DOUBLE_CLICK_DELAY],
  );

  // 마우스 클릭시 함수
  const handleMouseDown = (event: ThreeEvent<PointerEvent>) => {
    // console.log('catRef.current :', catRef.current);
    // console.log('event.object :', event.object);
    // console.log('event.object.parent :', event.object.parent);
    // console.log('event.object.parent?.parent :', event.object.parent?.parent);

    const isCatModel =
      event.object.parent?.parent === catRef.current ||
      event.object.parent === catRef.current;

    if (event.button === 0 && isCatModel) {
      // 좌클릭이고 고양이 모델일 때만
      setMenumbar(false);
      setTalking(false);
      setIsDragging(true);
      window.addEventListener('mousemove', handleWindowMouseMove);
      window.addEventListener('mouseup', handleWindowMouseUp);
    }
  };

  // 마우스를 클릭하고 있다가 놓았을 때
  const handleWindowMouseUp = () => {
    if (!catRef.current) return;

    // stopAnimation();

    setIsDragging(false);
    moveYzero();
    window.removeEventListener('mousemove', handleWindowMouseMove);
    window.removeEventListener('mouseup', handleWindowMouseUp);
  };

  // 위에서 고양이 내려놓을 때 바닥으로 천천히 움직이기
  const moveYzero = useCallback(() => {
    if (!catRef.current || catRef.current.position.y === 0) return;

    const startY = catRef.current.position.y;
    const startTime = Date.now();
    const targetY = 0;
    const duration = startY > 10 ? 1500 : 150 * startY;

    // `z_swim` 애니메이션 시작
    if (controllerRef.current && startY > 3) {
      // controllerRef.current.stopAnimation();
      controllerRef.current.playAnimation('z_swim', {
        duration: MOVEMENT_CONFIG.ANIMATION_DURATION,
        loop: THREE.LoopRepeat,
      });
      setNowAnimation('z_swim');
    }

    const animateDown = () => {
      if (!catRef.current) return;

      const currentTime = Date.now();
      const elapsed = currentTime - startTime;
      const progress = Math.min(elapsed / duration, 1);
      const easeOutProgress = 1 - Math.pow(1 - progress, 2);

      // y 위치 업데이트
      catRef.current.position.setY(
        startY + (targetY - startY) * easeOutProgress,
      );

      if (progress < 1) {
        requestAnimationFrame(animateDown);
      } else {
        catRef.current.position.setY(0); // 정확히 0으로 설정

        // `z_swim` 종료 후 `idle`로 전환
        if (
          catRef.current &&
          controllerRef.current &&
          catRef.current.position.x > targetX
        ) {
          controllerRef.current.stopAnimation();
          controllerRef.current.playAnimation('z_left_nomove', {
            duration: MOVEMENT_CONFIG.ANIMATION_DURATION,
            loop: THREE.LoopRepeat,
          });
          setNowAnimation('z_left_nomove');
        } else if (
          catRef.current &&
          controllerRef.current &&
          catRef.current.position.x < targetX
        ) {
          controllerRef.current.stopAnimation();
          controllerRef.current.playAnimation('z_right_nomove', {
            duration: MOVEMENT_CONFIG.ANIMATION_DURATION,
            loop: THREE.LoopRepeat,
          });
          setNowAnimation('z_right_nomove');
        } else {
          if (controllerRef.current) {
            controllerRef.current.stopAnimation();
            controllerRef.current.playAnimation('a_idle', {
              duration: MOVEMENT_CONFIG.ANIMATION_DURATION,
              loop: THREE.LoopRepeat,
            });
            setNowAnimation('a_idle');
          }
        }
      }
    };

    animateDown();
  }, [catRef, controllerRef, setNowAnimation, fullSize]);

  useEffect(() => {
    if (scene) {
      for (let i = 0; i < scene.children.length; i++) {
        const child = scene.children[i];
        if (child.name.includes('Cat')) {
          catRef.current = child as THREE.Group;
          console.log('for 문에서 Cat 이 이름에 있었음');
          console.log(catRef.current);
          break; // 조건이 충족되면 순회를 중단합니다.
        }
      }
      // 기존 코드
      // catRef.current = scene.children[0] as THREE.Group;
      if (catRef.current) {
        console.log(catRef.current.position);
        initialRotation.current = catRef.current.rotation.clone();
        // console.log('initialRotation.current:', initialRotation.current);
        // console.log('catRef.current.rotation:', catRef.current.rotation);
      }
    }
  }, [scene]);

  useEffect(() => {
    const Ids: number[] = [
      wear.armItemSerialNumber,
      wear.backgroundSerialNumber,
      wear.bodyItemSerialNumber,
      wear.headItemSerialNumber,
      // wear.characterSerialNumber,
    ];
    console.log('Ids : ', Ids);
    for (const i in nodes) {
      const node = nodes[i];
      // console.log(node);

      // 일단 캐릭터 빼고 다 안보이게 하기
      if (node.userData?.objID >= 50) node.visible = false;

      if (node.name === 'Scene') node.visible = true;
      else if (Ids.includes(node.userData?.objID)) {
        console.log('포함했음 :', node);
        for (let j = 0; j < node.children.length; j++) {
          // console.log(node.children[j]);
          // node.children[j].visible = true;
        }
        node.visible = true;
      }
    }
    scene.visible = true;
  }, [wear]);
  // 초기 각도 설정
  useEffect(() => {
    const maintainRotation = () => {
      if (catRef.current && initialRotation.current) {
        catRef.current.rotation.copy(initialRotation.current);
      }
      requestAnimationFrame(maintainRotation);
    };

    maintainRotation();
    setIgnoreMouseEvent(true);
  }, []);

  // 초기 애니메이션 설정
  useEffect(() => {
    if (scene && catRef.current && animations.length > 0) {
      initialRotation.current = catRef.current.rotation.clone();

      // 애니메이션 컨트롤러 초기화 및 실행
      controllerRef.current = new AnimationController(
        catRef.current,
        animations,
      );
      controllerRef.current.playAnimation('a_idle', {
        duration: 0.016,
        loop: THREE.LoopRepeat,
      });

      // 초기 애니메이션 상태 설정
      setNowAnimation('a_idle');
    }

    console.log(animations);
    console.log(scene);
    // cleanup
    return () => {
      if (controllerRef.current) {
        controllerRef.current.stopAnimation();
      }
    };
  }, [scene, animations]);

  const handleRightClick = (e: ThreeEvent<PointerEvent>) => {
    e.stopPropagation();
    if (menubar) {
      setMenumbar(false);
      return;
    }
    if (talking) {
      setTalking(false);
      return;
    }

    const vector = new THREE.Vector3();
    vector.setFromMatrixPosition(e.object.matrixWorld);
    vector.project(e.camera);

    let x = (vector.x * 0.5 + 0.5) * window.innerWidth - menuSize.width / 2;
    const y =
      (-vector.y * 0.5 + 0.5) * window.innerHeight - menuSize.height - 150;

    console.log('x :', x, 'y :', y);
    if (window.innerWidth - menuSize.width < x) {
      x = window.innerWidth - menuSize.width;
    } else if (x < 0) {
      x = 0;
    }
    setMenubarPos({ x, y });
    setMenumbar(true);
  };

  interface MovementState {
    isMoving: boolean;
    animationFrame: number | null;
  }

  const getRandomInt = (min: number, max: number): number => {
    const roundedMin = Math.ceil(min);
    const roundedMax = Math.floor(max);
    return (
      Math.floor(Math.random() * (roundedMax - roundedMin + 1)) + roundedMin
    );
  };
  type CharacterRef = React.MutableRefObject<THREE.Group | null>;
  // 상수 정의
  const MOVEMENT_CONFIG = {
    SPEED: 2,
    ANIMATION_DURATION: 0.016,
    SCALE_FACTOR: 80,
    COMPLETION_THRESHOLD: 0.99,
    BOUNDARY_OFFSET: 0.5,
  };
  // 타이머 참조 추가

  const useCharacterMovement = (
    catRef: CharacterRef,
    controllerRef: React.MutableRefObject<AnimationController | null>,
    setNowAnimation: (animation: string) => void,
    fullSize: { width: number; height: number },
  ) => {
    const movementState = useRef<MovementState>({
      isMoving: false,
      animationFrame: null,
    });

    const idleTimerRef = useRef<NodeJS.Timeout | null>(null);

    const cleanupCurrentMovement = useCallback(() => {
      if (movementState.current.animationFrame !== null) {
        cancelAnimationFrame(movementState.current.animationFrame);
        movementState.current.animationFrame = null;
      }
      movementState.current.isMoving = false;
    }, []);

    const playMovementAnimation = useCallback(
      (isMovingRight: boolean) => {
        if (!controllerRef.current) return;

        const animation = isMovingRight ? 'z_right_nomove' : 'z_left_nomove';
        controllerRef.current.stopAnimation();
        controllerRef.current.playAnimation(animation, {
          duration: MOVEMENT_CONFIG.ANIMATION_DURATION,
          loop: THREE.LoopRepeat,
        });
        setNowAnimation(animation);
      },
      [controllerRef, setNowAnimation],
    );

    const playIdleAnimation = useCallback(() => {
      if (!controllerRef.current) return;

      if (idleTimerRef.current) {
        clearTimeout(idleTimerRef.current);
        idleTimerRef.current = null;
      }

      controllerRef.current.stopAnimation();
      controllerRef.current.playAnimation('a_idle', {
        duration: MOVEMENT_CONFIG.ANIMATION_DURATION,
        loop: THREE.LoopRepeat,
      });
      setNowAnimation('a_idle');

      // isDragging이나 menubar가 true일 때는 다음 움직임을 예약하지 않음
      if (!isDragging && !menubar && !talking && !newAlarm) {
        idleTimerRef.current = setTimeout(() => {
          characterMove();
        }, 3000);
        setNewAlarm(false);
      }
    }, [
      controllerRef,
      setNowAnimation,
      isDragging,
      menubar,
      talking,
      newAlarm,
    ]);

    const characterMove = useCallback(() => {
      if (!catRef.current || !controllerRef.current) return;
      // isDragging이나 menubar가 true일 때는 움직임 시작하지 않음
      if (isDragging || menubar || talking) {
        playIdleAnimation();
        return;
      }

      if (movementState.current.isMoving) {
        cleanupCurrentMovement();
      }

      const boundaries = {
        min:
          -fullSize.width / 2 / MOVEMENT_CONFIG.SCALE_FACTOR +
          MOVEMENT_CONFIG.BOUNDARY_OFFSET,
        max:
          fullSize.width / 2 / MOVEMENT_CONFIG.SCALE_FACTOR -
          MOVEMENT_CONFIG.BOUNDARY_OFFSET,
      };

      targetX = getRandomInt(boundaries.min, boundaries.max);
      const startX = catRef.current.position.x;
      const distance = Math.abs(targetX - startX);
      const duration = (distance / MOVEMENT_CONFIG.SPEED) * 1000;
      const startTime = performance.now();
      const isMovingRight = targetX > startX;

      movementState.current.isMoving = true;
      playMovementAnimation(isMovingRight);

      const animate = (currentTime: number) => {
        if (!catRef.current || !movementState.current.isMoving) return;

        const elapsed = currentTime - startTime;
        const progress = Math.min(elapsed / duration, 1);

        const easedProgress = easeInOutQuad(progress);
        const newX = startX + (targetX - startX) * easedProgress;
        catRef.current.position.setX(newX);

        if (progress < MOVEMENT_CONFIG.COMPLETION_THRESHOLD) {
          movementState.current.animationFrame = requestAnimationFrame(animate);
        } else {
          catRef.current.position.setX(targetX);
          playIdleAnimation();
          cleanupCurrentMovement();
        }
      };

      movementState.current.animationFrame = requestAnimationFrame(animate);
    }, [
      catRef,
      controllerRef,
      fullSize,
      cleanupCurrentMovement,
      playMovementAnimation,
      playIdleAnimation,
      isDragging,
      menubar,
      talking,
    ]);

    useEffect(() => {
      if (isDragging || menubar || talking) {
        cleanupCurrentMovement();
        playIdleAnimation();
      } else if (newAlarm) {
        playAlertAnimation(alarmAnimationId);
      } else {
        characterMove();
      }
    }, [
      isDragging,
      menubar,
      talking,
      newAlarm,
      cleanupCurrentMovement,
      playIdleAnimation,
      characterMove,
    ]);

    useEffect(() => {
      if (!isDragging && !menubar) {
        if (catRef.current && catRef.current.position.y > 0.001) {
          moveYzero();
        } else {
          characterMove();
        }
      }

      return () => {
        if (idleTimerRef.current) {
          clearTimeout(idleTimerRef.current);
        }
        cleanupCurrentMovement();
      };
    }, [characterMove, cleanupCurrentMovement, moveYzero]);

    useEffect(() => {
      if (newAlarm && catRef.current && controllerRef.current) {
        cleanupCurrentMovement();
        catRef.current.position.x = 5;
        controllerRef.current.playAnimation(animations[alarmAnimationId].name, {
          duration: MOVEMENT_CONFIG.ANIMATION_DURATION,
          loop: THREE.LoopOnce,
          onComplete: () => {
            if (
              controllerRef.current &&
              catRef.current &&
              initialRotation.current
            ) {
              catRef.current.rotation.copy(initialRotation.current);
              controllerRef.current.playAnimation('a_idle', {
                duration: MOVEMENT_CONFIG.ANIMATION_DURATION,
                loop: THREE.LoopRepeat,
              });
              setNowAnimation('a_idle');
            }
          },
        });
        setNowAnimation(animations[alarmAnimationId].name);
      }
    }, [newAlarm, cleanupCurrentMovement, animations, alarmAnimationId]);

    const easeInOutQuad = (t: number): number => {
      return t < 0.5 ? 2 * t * t : 1 - Math.pow(-2 * t + 2, 2) / 2;
    };

    return { characterMove, moveYzero };
  };

  useCharacterMovement(
    catRef as CharacterRef,
    controllerRef as ControllerRef,
    setNowAnimation,
    fullSize,
  );

  // Alert 애니메이션 실행 함수
  const playAlertAnimation = useCallback(
    async (alertValue: number) => {
      if (!catRef.current || !controllerRef.current) {
        console.warn('References not initialized');
        return;
      }

      setAlwaysDisplay(true);
      // Move character to x = 5
      catRef.current.position.x = 5;

      // Stop any current animation
      controllerRef.current.stopAnimation();

      // Find and play the alert animation
      const alertAnimation = animations[alertValue];
      if (!alertAnimation) {
        console.warn(`No animation found for alert value: ${alertValue}`);
        return;
      }

      try {
        // Play alert animation with a clear completion handler
        controllerRef.current.playAnimation(alertAnimation.name, {
          duration: MOVEMENT_CONFIG.ANIMATION_DURATION,
          loop: THREE.LoopOnce,
          onComplete: () => {
            // Always return to idle animation after alert
            if (controllerRef.current) {
              controllerRef.current.playAnimation('a_idle', {
                duration: MOVEMENT_CONFIG.ANIMATION_DURATION,
                loop: THREE.LoopRepeat,
              });
              setNowAnimation('a_idle');

              // Optional: Reset the alarm state
              // setNewAlarm(false);
              // setAlwaysDisplay(false);
            }
          },
        });
      } catch (error) {
        console.error('Error playing alert animation:', error);
        // Fallback to idle animation if something goes wrong
        if (controllerRef.current) {
          controllerRef.current.playAnimation('a_idle', {
            duration: MOVEMENT_CONFIG.ANIMATION_DURATION,
            loop: THREE.LoopRepeat,
          });
          setNowAnimation('a_idle');
          // setNewAlarm(false);
          // setAlwaysDisplay(false);
        }
      }
    },
    [
      animations,
      catRef,
      controllerRef,
      setNowAnimation,
      setNewAlarm,
      setAlwaysDisplay,
    ],
  );
  // Alarm 구독하고 Alaram 변경될때마다 무언가 실행됨
  useEffect(() => {
    // 이벤트 구독 함수 정의
    const handleAlertChange = (event: string) => {
      const changeAnimationId = async () => {
        const newValue: number | number[] = JSON.parse(event);
        // setAlarmPos
        if (catRef.current) {
          const vector = new THREE.Vector3();
          vector.setFromMatrixPosition(catRef.current.matrixWorld);

          vector.project(camera);
          const x = window.innerWidth / 2;
          const y =
            (-vector.y * 0.5 + 0.5) * window.innerHeight -
            alarmSize.height -
            200;

          setAlarmPos({ x: x + 400 - alarmSize.width / 2, y });
        }

        console.log('newValue:', newValue);
        // 배열인 경우 마지막 요소 사용
        if (Array.isArray(newValue)) {
          console.log(
            '배열에서 마지막 값 실행:',
            newValue[newValue.length - 1],
          );
          await setAlarmAnimationId(newValue[newValue.length - 1]);
        } else {
          console.log('단일 값 실행:', newValue);
          await setAlarmAnimationId(newValue);
        }
        setNewAlarm(true);
        setMenumbar(false);
      };
      changeAnimationId();
    };

    // 이벤트 리스너 등록
    // @ts-ignore
    window.electron.onStoreAlertChange(handleAlertChange);

    // Cleanup 함수: 컴포넌트 언마운트 또는 의존성 변경 시 이전 리스너 제거
    return () => {
      // @ts-ignore
      window.electron?.removeStoreAlertChange?.(handleAlertChange);
    };
  }, []);

  return (
    <>
      <CameraControls ref={cameraControlsRef} />

      {scene && (
        <primitive
          object={scene}
          onPointerEnter={() => setIgnoreMouseEvent(false)}
          onPointerLeave={() => setIgnoreMouseEvent(true)}
          onClick={handleClick}
          onPointerDown={(e: ThreeEvent<PointerEvent>) => {
            handleMouseDown(e);
          }}
          onPointerUp={() => {
            handleWindowMouseUp();
          }}
          // onPointerMove={handlePointerMove}
          // onClick={(error: React.MouseEvent) => console.log(error)}
          onContextMenu={(e: ThreeEvent<PointerEvent>) => {
            e.stopPropagation(); // 기본 우클릭 메뉴 방지
            // console.log(e);
            handleRightClick(e); // 기존 우클릭 메뉴
          }}
        />
      )}
    </>
  );
};

export default NewBear15;

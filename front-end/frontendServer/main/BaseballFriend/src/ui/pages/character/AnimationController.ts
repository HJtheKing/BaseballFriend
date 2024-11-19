import * as THREE from 'three';

class AnimationController {
  private mixer: THREE.AnimationMixer;
  private animations: THREE.AnimationClip[];
  private currentAction: THREE.AnimationAction | null = null;
  private animationId: number | null = null;
  private object: THREE.Object3D;
  private initialRotation: THREE.Euler | null = null;
  private eventListeners: {
    type: keyof THREE.AnimationMixerEventMap;
    listener: (event: THREE.Event) => void;
  }[] = [];

  constructor(object: THREE.Object3D, animations: THREE.AnimationClip[]) {
    this.object = object;
    this.animations = animations;
    this.mixer = new THREE.AnimationMixer(object);
    this.initialRotation = object.rotation.clone();
  }

  playAnimation(
    animationName: string,
    options: {
      duration?: number;
      loop?: THREE.AnimationActionLoopStyles; // loop 타입 명시
      onComplete?: () => void;
    } = {},
  ) {
    const { duration = 0.024, loop = THREE.LoopRepeat, onComplete } = options;

    // 이전 애니메이션 정리
    if (this.currentAction) {
      this.currentAction.stop();
    }
    if (this.animationId !== null) {
      cancelAnimationFrame(this.animationId);
    }
    this.clearEventListeners();

    // 새 애니메이션 설정
    const clip = THREE.AnimationClip.findByName(this.animations, animationName);
    if (!clip) {
      console.warn(`Animation "${animationName}" not found`);
      return;
    }

    this.currentAction = this.mixer.clipAction(clip);
    this.currentAction.setLoop(loop, Infinity); // 루프 설정
    this.currentAction.clampWhenFinished = true; // 애니메이션 완료 시 정지 상태 유지
    this.currentAction.play();

    // 애니메이션 완료 시 이벤트 처리
    if (loop === THREE.LoopOnce && onComplete) {
      const listener = (event: THREE.Event) => {
        if (event.type === 'finished') {
          this.stopAnimation();
          onComplete();
        }
      };
      const eventType: keyof THREE.AnimationMixerEventMap = 'finished'; // 타입 단언
      this.mixer.addEventListener(eventType, listener);
      this.eventListeners.push({ type: eventType, listener });
    }

    // 애니메이션 루프 시작
    const animate = () => {
      this.mixer.update(duration);
      if (this.initialRotation) {
        this.object.rotation.copy(this.initialRotation);
      }
      this.animationId = requestAnimationFrame(animate);
    };

    animate();
  }

  stopAnimation() {
    if (this.currentAction) {
      this.currentAction.stop();
    }
    if (this.animationId !== null) {
      cancelAnimationFrame(this.animationId);
      this.animationId = null;
    }
    this.mixer.stopAllAction();
    this.clearEventListeners(); // 이벤트 리스너 정리
  }

  private clearEventListeners() {
    this.eventListeners.forEach(({ type, listener }) => {
      this.mixer.removeEventListener(type, listener);
    });
    this.eventListeners = []; // 모든 리스너 정보 초기화
  }
}

export default AnimationController;

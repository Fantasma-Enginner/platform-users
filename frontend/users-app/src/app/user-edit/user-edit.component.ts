import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { Subject } from 'rxjs';
import { Observable } from 'rxjs';
import { WebcamImage, WebcamInitError } from 'ngx-webcam';

import { AuthenticationDTO, UsuariosService, UserDetailDTO, UserDTO, ProfileDTO, PerfilesService, EnrollmentDTO } from '@tsir/users-api';
import { TollDTO, EstacionesService } from '@tsir/tolls-api';
import { ActivatedRoute } from '@angular/router';
import { MustMatch } from 'src/app/helpers/must-match.validator';

@Component({
  selector: 'users-user-edit',
  templateUrl: './user-edit.component.html',
  styleUrls: ['./user-edit.component.css']
})
export class UserEditComponent implements OnInit, OnChanges {

  registerForm: FormGroup;
  enrolmentForm: FormGroup;
  authenticationForm: FormGroup;
  showWebcam = false;
  message?: string;
  private error = false;
  private success = false;

  editMode: boolean;
  @Input() id: number;

  constructor(
    private route: ActivatedRoute,
    private usuariosService: UsuariosService,
    private profileService: PerfilesService,
    private tollService: EstacionesService,
    private formBuilder: FormBuilder
  ) { }

  public videoOptions: MediaTrackConstraints = {
    width: { ideal: 320 },
    height: { ideal: 240 }
  };
  public errors: WebcamInitError[] = [];

  profiles: ProfileDTO[] = [];

  tolls: TollDTO[] = [];

  // latest snapshot
  public imageData: string = null;

  // webcam snapshot trigger
  private trigger: Subject<void> = new Subject<void>();

  public actionVideo = 'fas fa-video';

  private validatorPhone = Validators.compose([Validators.pattern('[0-9]*'), Validators.minLength(7), Validators.maxLength(10)]);
  private validatorName = Validators.pattern('[a-zA-Z ]*');

  ngOnInit() {
    this.editMode = this.id !== 0;
    this.setSecurityConfiguration();
    this.initRegisterForm();
    this.initEnrollmentForm();
    this.initAuthenticationForm();
    this.checkData();

  }

  ngOnChanges(changes: SimpleChanges): void {
    for (const property in changes) {
      if (property === 'id') {
        this.editMode = this.id !== 0;
        this.checkData();
      }
    }
  }

  private setSecurityConfiguration(): void {
    this.profileService.configuration.accessToken = localStorage.getItem('token');
    this.tollService.configuration.accessToken = localStorage.getItem('token');
    this.usuariosService.configuration.accessToken = localStorage.getItem('token');
  }

  private checkData() {
    if (this.editMode) {
      this.loadProfiles();
      this.loadTolls();
      this.loadUserData();
    }
  }

  private loadProfiles() {
    if (!this.profiles) {
      this.profileService.findProfiles().subscribe(
        (data) => {
          this.profiles = data;
        }, (error) => {
          this.setError('Ha ocurrido un error en la carga del formulario');
        }
      );
    }
  }

  private loadTolls(): void {
    this.tollService.findTolls().subscribe(
      (data) => {
        this.tolls = data;
      },
      (error) => {
        this.setError('Ha ocurrido un error en la carga de estaciones');
      }
    );
  }

  private initRegisterForm() {
    this.registerForm = this.formBuilder.group({
      user: this.formBuilder.group({
        code: [{ value: '', disabled: this.editMode }, Validators.required],
        firstName: ['', [Validators.required, this.validatorName]],
        lastName: ['', [Validators.required, this.validatorName]],
        photo: ['', Validators.required]
      }),
      contact: this.formBuilder.group({
        phone: ['', this.validatorPhone],
        address: [''],
        email: ['', [Validators.email]],
        emergencyName: ['', this.validatorName],
        emergencyPhone: ['', this.validatorPhone]
      }),
      checkPolicies: ['', this.editMode ? Validators.nullValidator : Validators.requiredTrue]
    });
  }

  private initEnrollmentForm() {
    this.enrolmentForm = this.formBuilder.group({
      profile: ['', Validators.required],
      toll: ['', Validators.required]
    });
  }

  private initAuthenticationForm() {
    this.authenticationForm = this.formBuilder.group({
      password: [''],
      confirmPassword: [''],
      cardId: [''],
      fingerprint: ['']
    }, {
      validator: MustMatch('password', 'confirmPassword')
    });
  }

  private loadUserData() {
    this.usuariosService.getUser(this.id).subscribe(
      (data: UserDetailDTO) => {
        this.loadForms(data);
      }
    );
  }

  private loadForms(data: UserDetailDTO) {
    this.registerForm.patchValue({
      user: data.user
    });
    if (data.user.photo) {
      this.imageData = `data:image/jpeg;base64,${data.user.photo}`;
    }
    if (data.contact) {
      this.registerForm.patchValue({
        contact: data.contact
      });
    }
    if (data.enrollment) {
      this.enrolmentForm.patchValue(
        data.enrollment
      );
    }
    if (data.authentication) {
      this.authenticationForm.patchValue(
        data.authentication
      );
    }
  }

  submitUser() {
    const userData = this.registerForm.value;
    if (this.editMode) {
      userData.user.code = this.id;
      this.updateUser(userData);
    } else {
      this.registerUser(userData);
    }

  }

  private registerUser(userData: UserDTO) {
    this.usuariosService.registerUser(userData).subscribe(
      (data) => {
        this.setSuccess('Usuario registrado satisfactoriamente');
        this.registerForm.reset();
        this.resetImage();
      },
      (err) => {
        this.setError('Ha ocurrido un error: ' + err.error.message);
      }
    );
  }

  private updateUser(userData: UserDTO): void {
    this.usuariosService.updateUser(userData, this.id).subscribe(
      (data) => {
        this.setSuccess('Usuario actualizado satisfactoriamente');
      },
      (err) => {
        this.setError('Ha ocurrido un error: ' + err.error.message);
        this.loadUserData();
      }
    );
  }

  submitEnrollment(): void {
    const enrollData: EnrollmentDTO = this.enrolmentForm.value;
    this.usuariosService.updateEnrollment(enrollData, this.id).subscribe(
      (data) => {
        this.setSuccess('Usuario actualizado satisfactoriamente');
      },
      (err) => {
        this.setError('Ha ocurrido un error: ' + err.error.message);
      }
    );
  }

  submitAuthentication(method: string): void {
    const authData: AuthenticationDTO = this.authenticationForm.value;
    this.usuariosService.updateAuthentication(authData, this.id).subscribe(
      () => {
        this.setSuccess(method + ' asignada correctamente');
      }
    );
  }

  asignCard() {
    this.submitAuthentication('Tarjeta');
  }

  resetPassword(): void {
    this.submitAuthentication('Contraseña');
  }

  switchVideo(): void {
    this.showWebcam = !this.showWebcam;
    if (this.showWebcam) {
      this.actionVideo = 'fas fa-camera-retro';
      this.resetImage();
    } else {
      this.actionVideo = 'fas fa-video';
      this.triggerSnapshot();
    }
  }

  public get triggerObservable(): Observable<void> {
    return this.trigger.asObservable();
  }

  public handleImage(webcamImage: WebcamImage): void {
    this.imageData = webcamImage.imageAsDataUrl;
    this.updatePhoto();
  }

  public handleInitError(error: WebcamInitError): void {
    this.errors.push(error);
    if (this.showWebcam) {
      this.switchVideo();
    }
  }

  public triggerSnapshot(): void {
    this.trigger.next();
  }

  numberOnly(event: any): boolean {
    const charCode = (event.which) ? event.which : event.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
      return false;
    }
    return true;
  }

  hexadecimalOnly(event: any): boolean {
    const charCode = (event.which) ? event.which : event.keyCode;
    if ((charCode >= 48 && charCode <= 57) || (charCode >= 65 && charCode <= 70) || (charCode >= 97 && charCode <= 102)) {
      return true;
    }
    return false;
  }

  private updatePhoto(): void {
    if (this.imageData != null) {
      this.registerForm.patchValue({
        user: {
          photo: this.imageData.replace('data:image/jpeg;base64,', '')
        }
      });
    } else {
      this.registerForm.patchValue({
        user: {
          photo: null
        }
      });
    }
  }

  resetImage(): void {
    this.imageData = null;
    this.updatePhoto();
  }

  hasImage(): boolean {
    return this.imageData != null;
  }

  onFileChanged(event: any) {
    if (this.showWebcam) {
      this.switchVideo();
    }
    const file = event.target.files[0];
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = (evt) => {
      this.imageData = reader.result.toString();
      this.updatePhoto();
    };
  }

  searchTisc() { }

  readTisc() { }

  isSuccess(): boolean {
    return this.success;
  }

  isError() {
    return this.error;
  }

  private setSuccess(message: string) {
    this.success = true;
    this.error = false;
    this.message = message;
  }

  private setError(message: string) {
    this.success = false;
    this.error = true;
    this.message = message;
  }

  clearMessage(): void {
    this.error = false;
    this.success = false;
    this.message = null;
  }

}

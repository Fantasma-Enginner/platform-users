import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { PerfilesService, ProfileDTO, State } from '@tsir/users-api';
import Swal from 'sweetalert2';

@Component({
  selector: 'users-profile-edit',
  templateUrl: './profile-edit.component.html',
  styleUrls: ['./profile-edit.component.css']
})
export class ProfileEditComponent implements OnInit {

  editMode?: boolean;
  id: number;

  message?: string;
  success = false;

  private validatorName = Validators.pattern('^[A-Z]{3,15}([ ]{1}[A-Z0-9_-]+)?$');
  registerForm: FormGroup;

  states: string[] = [State.NUEVO, State.ACTIVO, State.INACTIVO, State.SUSPENDIDO];

  toast = Swal.mixin({
    toast: true,
    position: 'bottom-right',
    showConfirmButton: false,
    timer: 3000,
    timerProgressBar: true,
    didOpen: (t) => {
      t.addEventListener('mouseenter', Swal.stopTimer);
      t.addEventListener('mouseleave', Swal.resumeTimer);
    }
  });

  constructor(
    private perfilesService: PerfilesService,
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private location: Location
  ) { }


  ngOnInit() {
    this.id = this.route.snapshot.params.id;
    this.editMode = this.id ? true : false;
    this.setSecurityConfiguration();
    this.checkData();
  }

  private setSecurityConfiguration(): void {
    this.perfilesService.configuration.accessToken = localStorage.getItem('token');
  }

  checkData(): void {
    if (!this.registerForm) {
      this.initRegisterForm();
    }
    if (this.editMode) {
      this.registerForm.controls.code.disable();
      this.loadProfileData();
    } else {
      this.registerForm.controls.code.enable();
      this.registerForm.reset();
    }
  }

  private initRegisterForm() {
    this.registerForm = this.formBuilder.group({
      identifier: [''],
      code: [{ value: '', disabled: this.editMode }, Validators.required],
      name: ['', [Validators.required, this.validatorName]],
      state: ['']
    });
  }

  numberOnly(event: any): boolean {
    const charCode = (event.which) ? event.which : event.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
      return false;
    }
    return true;
  }

  submitProfile() {
    const data = this.registerForm.getRawValue();
    if (this.editMode) {
      this.updateProfile(data);
    } else {
      this.registerProfile(data);
    }
  }

  private registerProfile(profileData: ProfileDTO) {
    this.perfilesService.registerProfile(profileData).subscribe(
      (data) => {
        this.setSuccess('Perfil registrado satisfactoriamente');
        this.registerForm.reset();
      }
    );
  }

  private updateProfile(profileData: ProfileDTO) {
    this.perfilesService.updateProfile(profileData, this.id).subscribe(
      (data) => {
        this.setSuccess('Perfil actualizado satisfactoriamente');
      },
      (err) => {
        this.loadProfileData();
      }
    );
  }

  private loadProfileData() {
    this.perfilesService.getProfile(this.id).subscribe(
      (data: ProfileDTO) => {
        this.loadForms(data);
      }
    );
  }

  private loadForms(data: ProfileDTO) {
    this.registerForm.setValue(data);
  }

  private setSuccess(message: string) {
    this.success = true;
    this.message = message;
    this.toast.fire({
      icon: 'success',
      title: 'Perfiles',
      text: message
    });
  }

  clearMessage() {
    this.success = false;
    this.message = null;
  }

  backLocation(): void {
    this.location.back();
  }

}

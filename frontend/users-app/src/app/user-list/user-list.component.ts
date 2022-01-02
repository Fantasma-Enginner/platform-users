import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { PerfilesService, ProfileDTO, UserDTO, UsuariosService, State } from '@tsir/users-api';
import { TollDTO, EstacionesService } from '@tsir/tolls-api';
import { Observable } from 'rxjs';

@Component({
  selector: 'users-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {

  constructor(
    private usersService: UsuariosService,
    private profilesService: PerfilesService,
    private formBuilder: FormBuilder
  ) { }

  users: UserDTO[];
  error?: string;
  searchForm: FormGroup;
  criteriaSelected: string;

  showEdit = false;
  userId = 0;

  profiles: ProfileDTO[];
  tolls: TollDTO[];
  criteriaList: any[] = [
    { label: '', key: '' },
    { label: 'Identificación', key: 'CODE' },
    { label: 'Nombre', key: 'FIRSTNAME' },
    { label: 'Apellido', key: 'LASTNAME' },
    { label: 'Estado', key: 'STATE' },
    { label: 'Perfil', key: 'PROFILE' },
    { label: 'Estación', key: 'TOLL' }
  ];
  stateList: string[] = [State.ACTIVO, State.INACTIVO, State.SUSPENDIDO];


  ngOnInit() {
    this.setSecurityConfiguration();
    this.initSearchForm();
    this.loadProfiles();
  }

  private setSecurityConfiguration(): void {
    this.profilesService.configuration.accessToken = localStorage.getItem('token');
    this.usersService.configuration.accessToken = localStorage.getItem('token');
  }

  private loadProfiles() {
    this.profilesService.findProfiles().subscribe(
      (data) => {
        this.profiles = data;
      }, (error) => {
        this.error = 'Ha ocurrido un error';
      }
    );
  }

  private initSearchForm() {
    this.searchForm = this.formBuilder.group({
      criteria: [''],
      value: ['']
    });
  }

  resetFilter() {
    this.searchForm.patchValue({
      value: ''
    });
  }

  private getToken() {
    return localStorage.getItem('token');
  }

  isSelected(key: string): boolean {
    return this.searchForm.controls['criteria'].value == key;
  }

  numberOnly(event: any): boolean {
    const charCode = (event.which) ? event.which : event.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
      return false;
    }
    return true;
  }

  getObservableFindUsers(): Observable<UserDTO[]> {
    let filter = this.searchForm.value;
    let map = {};
    if (filter.criteria) {
      map[filter.criteria] = filter.value;
      return this.usersService.findUsers(map);
    } else {
      return this.usersService.findUsers();
    }
  }

  findUsers(): void {
    this.getObservableFindUsers().subscribe(
      (data: UserDTO[]) => {
        data.forEach(user => {
          if (user.photo != null) {
            user.photo = `data:image/jpeg;base64,${user.photo}`
          }
        });
        this.users = data;
        this.error = null;
      },
      (error) => {
        this.error = 'No ha sido posible obtener resultados';
      }
    )
  }

  setUserId(userId: number): void {
    this.userId = userId;
    this.showEdit = true;
  }

  closeEdit(): void {
    this.showEdit = false;
  }

  clearMessage(): void {
    this.error = null;
  }

}

import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-pagination',
  templateUrl: './pagination.component.html',
  styleUrls: ['./pagination.component.css']
})
export class PaginationComponent implements OnInit {

  @Input() itemsNumber: number;
  @Input() allPagesNumber: number;
  @Input() itemsOptions: number[];
  @Output() changePage: EventEmitter<number> = new EventEmitter<number>();
  private _currentPage: number = 1;
  
  @Output() changeItems: EventEmitter<number> = new EventEmitter<number>();
  private _items: number = 5;

  constructor() { }

  ngOnInit(): void {}

  get currentPage(): number {
    return this._currentPage;
  }

  set currentPage(page) {
    this._currentPage = page;
    this.changePage.emit(this.currentPage);
  }

  onSetPage(event): void {
    this.currentPage = event.target.value;
  }

  get items(): number {
    return this._items;
  }

  set items(valItems) {
    this._items = valItems;
    this.changeItems.emit(this.items);
  }

  onSetItems(event): void {
    this.items = event;
  }

  onFirstPage(): void {
    this.currentPage = 1;
  }

  onLastPage(): void {
    this.currentPage = this.allPagesNumber;
  }

  onNextPage(): void {
    this.currentPage += 1;
  }

  onPreviousPage(): void {
    this.currentPage -= 1;
  }
}